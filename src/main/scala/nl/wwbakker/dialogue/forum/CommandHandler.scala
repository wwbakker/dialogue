package nl.wwbakker.dialogue.forum

import nl.wwbakker.dialogue.forum.persistence.FileHandler
import nl.wwbakker.dialogue.model.{SuccessOperation, SuccessOperationWithMessage}
import nl.wwbakker.dialogue.model.ids.AssertionId
import nl.wwbakker.dialogue.model.relation.RelationType
import nl.wwbakker.dialogue.utilities.Extensions.ErrorMessage
import nl.wwbakker.dialogue.utilities.Extensions._

object CommandHandler {
  def handleCommand(args : Seq[String], commandPrefix : String = "") : Either[ErrorMessage, SuccessOperation] = {
    loadFromDisk().flatMap{ case (forum, dw) =>
      args.toList match {
        case "new-assertion" :: text :: Nil =>
          dw.newAssertion(text)
        case "add-assertion" :: otherAssertionIdString :: otherAssertionRelationTypeString :: text :: Nil =>
          for {
            otherAssertionId <- AssertionId.format.parseFromString(otherAssertionIdString)
            otherAssertionRelationType <- RelationType.parse(otherAssertionRelationTypeString)
            assertionResult <- dw.addAssertion(text, relatesTo = otherAssertionId, relationType = otherAssertionRelationType)
          } yield assertionResult
        case "supersede-assertion" :: supersededAssertionIdString :: text :: incorporatedIdsStrings =>
          for {
            supersededAssertionId <- AssertionId.format.parseFromString(supersededAssertionIdString)
            incorporatedIds <- incorporatedIdsStrings.applyList(AssertionId.format.parseFromString)
            assertionResult <- dw.supersedeAssertion(supersededAssertionId, text, incorporatedIds)
          } yield assertionResult
        case "list" :: Nil =>
          Right(SuccessOperationWithMessage(forum.topicWithHistoryFormatted))
        case _ =>
          Left(
            s"""$commandPrefix new-assertion [text]
              |$commandPrefix add-assertion [other-assertion-id] [Supports/Invalidates/Detracts/Explains] [text]
              |$commandPrefix supersede-assertion [superseded-assertion-id] [text] [incorporated-assertion-ids, 0 or more]""".stripMargin
          )
      }
    }
  }

  def loadFromDisk(): Either[String, (Forum, DialogueWriter)] = {
    val ph = new FileHandler()
    ph.read() match {
      case Left(error) => Left(error)
      case Right(events) =>
        val forum = new Forum(events)
        val dw = new DialogueWriter(forum, persistenceHandler = ph)
        Right((forum, dw))
    }
  }

}
