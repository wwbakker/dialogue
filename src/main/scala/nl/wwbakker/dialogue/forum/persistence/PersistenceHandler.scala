package nl.wwbakker.dialogue.forum.persistence

import nl.wwbakker.dialogue.model.SuccessOperation
import nl.wwbakker.dialogue.model.events.Event
import play.api.libs.json.Json

import scala.util.Try

trait PersistenceHandler {
  type ErrorMessage = String

  def write(e: Event) : Either[ErrorMessage, SuccessOperation.type] = {
    Try(System.out.println(Json.stringify(Json.toJson(e)))).toEither
      .swap.map(_.toString).swap
      .map(_ => SuccessOperation)
  }

  def read() : Either[ErrorMessage, Seq[Event]]
}
