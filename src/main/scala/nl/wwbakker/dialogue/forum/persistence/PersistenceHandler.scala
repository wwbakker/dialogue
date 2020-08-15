package nl.wwbakker.dialogue.forum.persistence

import nl.wwbakker.dialogue.model.SuccessOperationWithoutMessage
import nl.wwbakker.dialogue.model.events.Event
import play.api.libs.json.Json

import scala.util.Try

trait PersistenceHandler {
  type ErrorMessage = String

  def write(e: Event) : Either[ErrorMessage, SuccessOperationWithoutMessage.type] = {
    Try(System.out.println(Json.stringify(Json.toJson(e)))).toEither
      .swap.map(_.toString).swap
      .map(_ => SuccessOperationWithoutMessage)
  }

  def read() : Either[ErrorMessage, Seq[Event]]
}
