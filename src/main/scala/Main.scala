import nl.wwbakker.dialogue.forum.{DialogueWriter, Forum}
import nl.wwbakker.dialogue.forum.persistence.FileHandler

object Main {

  def main(args: Array[String]): Unit = {

  }

  def loadFromDisk(): Either[String, (Forum, DialogueWriter)] = {
    val ph = new FileHandler()
    ph.read() match {
      case Left(error) => Left(error)
      case Right(events) =>
        val forum = new Forum(events)
        val dw = new DialogueWriter(forum, persistenceHandler = ph)
        Right((forum, dw))
    }
  }

}
