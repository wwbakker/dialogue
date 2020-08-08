import nl.wwbakker.dialogue.forum.{DialogueWriter, Forum}
import nl.wwbakker.dialogue.forum.persistence.FileHandler
import nl.wwbakker.dialogue.model.SuccessOperation
import nl.wwbakker.dialogue.model.ids.AssertionId
import nl.wwbakker.dialogue.model.relation.RelationType
import nl.wwbakker.dialogue.utilities.Extensions._

object Main {

  def main(args: Array[String]): Unit = {
    val r : Either[ErrorMessage, SuccessOperation.type] = loadFromDisk().flatMap{ case (_, dw) =>
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
              incorporatedIds <- incorporatedIdsStrings.parseList(AssertionId.format.parseFromString)
              assertionResult <- dw.supersedeAssertion(supersededAssertionId, text, incorporatedIds)
            } yield assertionResult
          case _ =>
            Left(
              """dialogue usage:
                |java -jar dialogue.jar [new-assertion] [text]
                |java -jar dialogue.jar [add-assertion] [other-assertion-id] [other-assertion-relation-type] [text]
                |java -jar dialogue.jar [supersede-assertion] [superseded-assertion-id] [text] [incorporated-assertion-ids, 0 or more]""".stripMargin
            )
        }
    }
    r.swap.foreach(System.err.println)
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
