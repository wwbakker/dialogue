package nl.wwbakker.dialogue.forum

import nl.wwbakker.dialogue.model.Assertion
import nl.wwbakker.dialogue.model.ids.AssertionId
import nl.wwbakker.dialogue.model.relation.Supersedes

object Validator {
  type ErrorMessage = String

  def validateIsFirstTopic(forum: Forum) : Option[ErrorMessage] =
    Some("There is already a topic active")
      .filter(_ => forum.assertions.nonEmpty)

  def validateAssertionConnected(forum : Forum, assertion : Assertion) : Option[ErrorMessage] =
    Some("An assertion needs to be connected to the original topic.")
      .filter(_ => forum.assertions.nonEmpty && assertion.relatesTo.isEmpty)


  def validateAssertionExists(forum : Forum, assertionId : AssertionId) : Option[ErrorMessage] =
    if (forum.assertions.exists(_.id == assertionId))
      None
    else
      Some(s"Assertion '$assertionId' doesn't exist")

  def validateAssertionIsNotAlreadySuperseded(forum : Forum, assertionId : AssertionId) : Option[ErrorMessage] =
    if (forum.assertions
      .exists(_.relatesTo.exists(a => a.relationType == Supersedes && a.relatedToAssertion == assertionId)))
      Some(s"Assertion '$assertionId' is already superseded")
    else
      None

}
