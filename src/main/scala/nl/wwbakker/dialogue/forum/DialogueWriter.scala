package nl.wwbakker.dialogue.forum

import nl.wwbakker.dialogue.forum.persistence.PersistenceHandler
import nl.wwbakker.dialogue.model.{Assertion, SuccessOperationWithoutMessage}
import nl.wwbakker.dialogue.model.events.{AssertionAdded, AssertionSuperseded}
import nl.wwbakker.dialogue.model.ids.AssertionId
import nl.wwbakker.dialogue.model.relation.{Relation, RelationType, Supersedes}
import nl.wwbakker.dialogue.utilities.Extensions._

class DialogueWriter(forum: Forum, persistenceHandler: PersistenceHandler) {
  type ErrorMessage = String

  def newAssertion(text: String): Either[ErrorMessage, SuccessOperationWithoutMessage.type] = {
    val newAssertion = Assertion(
      id = forum.generateAssertionId(),
      text = text,
      relatesTo = Nil
    )
    for {
      _ <- Validator.validateIsFirstTopic(forum).toLeft(SuccessOperationWithoutMessage)
      success <- persistenceHandler.write(AssertionAdded(newAssertion))
    } yield success
  }

  def addAssertion(text: String, relatesTo: AssertionId, relationType: RelationType): Either[ErrorMessage, SuccessOperationWithoutMessage.type] = {
    val newAssertion = Assertion(
      id = forum.generateAssertionId(),
      text = text,
      relatesTo = List(
        Relation(forum.generateRelationId(), relatesTo, relationType)
      )
    )
    for {
      success <- persistenceHandler.write(AssertionAdded(newAssertion))
    } yield success
  }

  def supersedeAssertion(oldAssertion: AssertionId,
                         text: String,
                         incorporatedAssertions : Seq[AssertionId]): Either[ErrorMessage, SuccessOperationWithoutMessage.type] = {
    val newAssertion = Assertion(
      id = forum.generateAssertionId(),
      text = text,
      relatesTo = List(
        Relation(forum.generateRelationId(), oldAssertion, Supersedes)
      )
    )
    for {
      _ <- Validator.validateAssertionExists(forum, oldAssertion).toLeft(SuccessOperationWithoutMessage)
      _ <- Validator.validateAssertionIsNotAlreadySuperseded(forum, oldAssertion).toLeft(SuccessOperationWithoutMessage)
      _ <- incorporatedAssertions.applyList(aid => Validator.validateAssertionExists(forum, aid).toLeft(SuccessOperationWithoutMessage))
      success <- persistenceHandler.write(AssertionSuperseded(oldAssertion, newAssertion, incorporatedAssertions))
    } yield success
  }
}
