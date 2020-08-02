package nl.wwbakker.dialogue.forum

import nl.wwbakker.dialogue.model.Assertion
import nl.wwbakker.dialogue.model.events.{AssertionAdded, AssertionSuperseded, Event}
import nl.wwbakker.dialogue.model.ids.AssertionId
import play.api.libs.json.Json

import scala.util.Try

class DialogueWriter(forum: Forum) {

  case class WriteSuccess()

  type ErrorMessage = String

  def addAssertion(assertion: Assertion): Either[ErrorMessage, WriteSuccess] = {
    for {
      _ <- Validator.validateAssertionConnected(forum, assertion).toLeft()
      success <- write(AssertionAdded(assertion))
    } yield success
  }

  def supersedeAssertion(oldAssertion: AssertionId,
                         newAssertion: Assertion,
                         incorporatedAssertions : Seq[AssertionId]): Either[ErrorMessage, WriteSuccess] = {
    for {
      _ <- Validator.validateAssertionExists(forum, oldAssertion).toLeft()
      _ <- Validator.validateAssertionIsNotAlreadySuperseded(forum, oldAssertion).toLeft()
      success <- write(AssertionSuperseded(oldAssertion, newAssertion, incorporatedAssertions))
    } yield success
  }

  def write(e: Event) : Either[ErrorMessage, WriteSuccess] = {
    Try(System.out.println(Json.stringify(Json.toJson(e)))).toEither
      .swap.map(_.toString).swap
      .map(_ => WriteSuccess())
  }
}
