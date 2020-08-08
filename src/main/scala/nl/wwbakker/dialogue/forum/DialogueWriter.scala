package nl.wwbakker.dialogue.forum

import nl.wwbakker.dialogue.forum.persistence.PersistenceHandler
import nl.wwbakker.dialogue.model.{Assertion, SuccessOperation}
import nl.wwbakker.dialogue.model.events.{AssertionAdded, AssertionSuperseded}
import nl.wwbakker.dialogue.model.ids.AssertionId
import nl.wwbakker.dialogue.model.relation.{Relation, RelationType, Supersedes}
import nl.wwbakker.dialogue.utilities.Extensions._

class DialogueWriter(forum: Forum, persistenceHandler: PersistenceHandler) {
  type ErrorMessage = String

  def newAssertion(text: String): Either[ErrorMessage, SuccessOperation.type] = {
    val newAssertion = Assertion(
      id = forum.generateAssertionId(),
      text = text,
      relatesTo = Nil
    )
    for {
      _ <- Validator.validateIsFirstTopic(forum).toLeft(SuccessOperation)
      success <- persistenceHandler.write(AssertionAdded(newAssertion))
    } yield success
  }

  def addAssertion(text: String, relatesTo: AssertionId, relationType: RelationType): Either[ErrorMessage, SuccessOperation.type] = {
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
                         incorporatedAssertions : Seq[AssertionId]): Either[ErrorMessage, SuccessOperation.type] = {
    val newAssertion = Assertion(
      id = forum.generateAssertionId(),
      text = text,
      relatesTo = List(
        Relation(forum.generateRelationId(), oldAssertion, Supersedes)
      )
    )
    for {
      _ <- Validator.validateAssertionExists(forum, oldAssertion).toLeft(SuccessOperation)
      _ <- Validator.validateAssertionIsNotAlreadySuperseded(forum, oldAssertion).toLeft(SuccessOperation)
      _ <- incorporatedAssertions.applyList(aid => Validator.validateAssertionExists(forum, aid).toLeft(SuccessOperation))
      success <- persistenceHandler.write(AssertionSuperseded(oldAssertion, newAssertion, incorporatedAssertions))
    } yield success
  }
}
