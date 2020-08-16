import nl.wwbakker.dialogue.discord.DiscordServer
import nl.wwbakker.dialogue.forum.CommandHandler
import nl.wwbakker.dialogue.model.{SuccessOperationWithMessage, SuccessOperationWithoutMessage}


object Main {
  def main(args: Array[String]): Unit = {
    args.headOption match {
      case Some("run-discord") =>
        DiscordServer.start()
      case _ =>
        val result = CommandHandler.handleCommand(args.toIndexedSeq, "java -jar dialogue.jar")
        System.err.println("dialogue usage:")
        result match {
          case Left(value) => System.err.println(value)
          case Right(SuccessOperationWithMessage(text)) => System.out.println(text)
          case Right(SuccessOperationWithoutMessage) => ()
        }
    }
  }



}
