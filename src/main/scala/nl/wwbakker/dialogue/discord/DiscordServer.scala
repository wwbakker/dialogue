package nl.wwbakker.dialogue.discord

import java.util.Base64

import os.Path
import ackcord._
import ackcord.commands.CommandController
import ackcord.data._
import ackcord.requests.{CreateMessage, CreateMessageData}
import cats.implicits.catsStdInstancesForFuture
import nl.wwbakker.dialogue.forum.CommandHandler
import nl.wwbakker.dialogue.model.{SuccessOperationWithMessage, SuccessOperationWithoutMessage}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration


object DiscordServer {
  val tokenPath: Path = os.home / ".dialogue" / "discord_token"
  val token: String = os.read(tokenPath)
  val clientId : UserId = UserId(token.split('.').headOption.map(Base64.getDecoder.decode).map(new String(_)).get)
  val clientSettings : ClientSettings = ClientSettings(token)

  import clientSettings.executionContext

  val client = Await.result(clientSettings.createClient(), Duration.Inf)


  def start(): Unit = {
    println(s"clientid: $clientId")


//    client.onEventAsync { implicit c => {
//      case APIMessage.MessageCreate(message, _) =>
//        val result = CommandHandler.handleCommand(message.content.split(" "))
////        val response = CreateMessage(message.channelId, params = CreateMessageData("done"))
////        client.requestsHelper.run(response).map(_ => ())
//        Future.unit
//    }
//    }

    def stripStartQuote(s : String) : String =
      if (s.startsWith("\""))
        s.substring(1)
      else
        s
    def stripEndQuote(s : String) : String =
      if (s.endsWith("\""))
        s.substring(0, s.length - 1)
      else
        s

    def stripQuotes(s : String) : String =
      stripEndQuote(stripStartQuote(s))

    def removeMentions(content : String) : String =
      content.replaceAll("<@![0-9]+> ","")

    lazy val registration = client.onEventSideEffects { implicit c => {
      case APIMessage.MessageCreate(message, _) if message.content != "stop" =>
        println(s"message: ${message.content} - ${message.mentions.mkString(", ")}" )
        if (message.mentions.contains(clientId)) {
          val result = CommandHandler.handleCommand(removeMentions(message.content)
            .split(" (?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)").map(stripQuotes),
            "@Wessel's Bot")
          val responseMessage = result match {
            case Left(errorMessage) => errorMessage
            case Right(SuccessOperationWithoutMessage) => "success"
            case Right(SuccessOperationWithMessage(text)) => text
          }
          val response = CreateMessage(message.channelId, params = CreateMessageData(responseMessage))
          client.requestsHelper.run(response).map(_ => ())
        }
      case APIMessage.MessageCreate(message, _) if message.content.contains("stop bot") =>
        println("stopping")
        stop()
        client.logout()
    }
    }

    def stop() : Unit = registration.stop()

    registration

    client.login()
  }

  //  class Listener(requests : Requests) extends CommandController(requests) {
  //    val command = TextChannelEvent
  //  }

  //  class DiscordCommandHandler(requests : Requests) extends CommandController(requests) {
  //
  //  }
}
