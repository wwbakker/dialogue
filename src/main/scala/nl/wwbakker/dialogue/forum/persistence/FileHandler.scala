package nl.wwbakker.dialogue.forum.persistence

import nl.wwbakker.dialogue.model.SuccessOperationWithoutMessage
import nl.wwbakker.dialogue.model.events.Event
import os.Path
import play.api.libs.json.Json

import scala.util.Try

class FileHandler extends PersistenceHandler {

  val path: Path = os.home / ".dialogue" / "history"

  override def write(e: Event) : Either[ErrorMessage, SuccessOperationWithoutMessage.type] =
    Try(os.write.append(target = path, data = Json.stringify(Json.toJson(e)) + System.lineSeparator(), createFolders = true)).toEither
      .swap.map(_.toString).swap
      .map(_ => SuccessOperationWithoutMessage)


  override def read() : Either[ErrorMessage, Seq[Event]] = {
    if (os.exists(path))
      Try(os.read.lines(path)
        .map(Json.parse)
        .map(Event.eventFormat.reads)
        .map(_.get))
        .toEither.swap.map(_.toString).swap
    else
      Right(Nil)
  }
}
