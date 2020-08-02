package nl.wwbakker.dialogue.forum.persistence

import nl.wwbakker.dialogue.model.events.Event
import play.api.libs.json.Json

import scala.util.Try

trait PersistenceHandler {
  type ErrorMessage = String
  type WriteSuccess = Unit

  def write(e: Event) : Either[ErrorMessage, WriteSuccess] = {
    Try(System.out.println(Json.stringify(Json.toJson(e)))).toEither
      .swap.map(_.toString).swap
      .map(_ => ())
  }

  def read() : Either[ErrorMessage, Seq[Event]]
}
