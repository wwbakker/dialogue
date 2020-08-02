package nl.wwbakker.dialogue.forum

import nl.wwbakker.dialogue.forum.persistence.PersistenceHandler
import nl.wwbakker.dialogue.model.Assertion
import nl.wwbakker.dialogue.model.events.{AssertionAdded, AssertionSuperseded}
import nl.wwbakker.dialogue.model.ids.AssertionId
import nl.wwbakker.dialogue.model.relation.{Relation, RelationType, Supersedes}

class DialogueWriter(forum: Forum, persistenceHandler: PersistenceHandler) {
  type WriteSuccess = ()
  type ErrorMessage = String

  def addAssertion(text: String, relatesTo: AssertionId, relationType: RelationType): Either[ErrorMessage, WriteSuccess] = {
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
                         incorporatedAssertions : Seq[AssertionId]): Either[ErrorMessage, WriteSuccess] = {
    val newAssertion = Assertion(
      id = forum.generateAssertionId(),
      text = text,
      relatesTo = List(
        Relation(forum.generateRelationId(), oldAssertion, Supersedes)
      )
    )
    for {
      _ <- Validator.validateAssertionExists(forum, oldAssertion).toLeft()
      _ <- Validator.validateAssertionIsNotAlreadySuperseded(forum, oldAssertion).toLeft()
      success <- persistenceHandler.write(AssertionSuperseded(oldAssertion, newAssertion, incorporatedAssertions))
    } yield success
  }
}
