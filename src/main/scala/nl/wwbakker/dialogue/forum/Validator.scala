package nl.wwbakker.dialogue.forum

import nl.wwbakker.dialogue.model.Assertion
import nl.wwbakker.dialogue.model.ids.AssertionId

object Validator {
  type ErrorMessage = String

  def validateAssertionConnected(forum : Forum, assertion : Assertion) : Option[ErrorMessage] =
    Some("An assertion needs to be connected to the original topic.")
      .filter(_ => forum.assertions.nonEmpty && assertion.relatesTo.isEmpty)


  def validateAssertionExists(forum : Forum, assertionId : AssertionId) : Option[ErrorMessage] = ???

  def validateAssertionIsNotAlreadySuperseded(forum : Forum, assertionId : AssertionId) : Option[ErrorMessage] = ???

}
