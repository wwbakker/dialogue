package nl.wwbakker.dialogue.discord

import java.util.Base64

import ackcord._
import ackcord.data._
import ackcord.requests.{CreateMessage, CreateMessageData, CreateReaction, Request}
import cats.implicits.catsStdInstancesForFuture
import nl.wwbakker.dialogue.forum.CommandHandler
import nl.wwbakker.dialogue.model.{SuccessOperationWithMessage, SuccessOperationWithoutMessage}
import os.Path

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.duration._


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
      case APIMessage.MessageCreate(message, _) if message.content.contains("stop bot") =>
        println("stopping")
        stop()
        client.logout()
        client.shutdownAckCord()
      case APIMessage.MessageCreate(message, _) =>
        println(s"message: ${message.content} - ${message.mentions.mkString(", ")}" )
        if (message.mentions.contains(clientId)) {
          val result = CommandHandler.handleCommand(removeMentions(message.content)
            .split(" (?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)").toIndexedSeq.map(stripQuotes),
            "@Wessel's Bot")
          result match {
            case Left(errorMessage) => send(CreateMessage(message.channelId, CreateMessageData(errorMessage)))
            case Right(SuccessOperationWithoutMessage) => send(CreateReaction(message.channelId, message.id, "\uD83D\uDC4D"))
            case Right(SuccessOperationWithMessage(text)) => send(CreateMessage(message.channelId, CreateMessageData(text)))
          }
        }
    }
    }

    def send[A](request : Request[A])(implicit c: CacheSnapshot) : Unit =
      client.requestsHelper.run(request).map(_ => ())

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
