package nl.wwbakker.dialogue.utilities

import play.api.libs.json.{JsString, Reads}

object Extensions {
  type ErrorMessage = String

  implicit class ReadExtension[A](r : Reads[A]) {
    def parseFromString(s : String) : Either[ErrorMessage, A] =
      r.reads(JsString(s)).asEither.swap.map(_.head._2.head.message).swap
  }

  implicit class ParseSeqExtensions[A, B](s : Seq[A]) {
    /**
     * Extension to convert a function that parses 1 value, into a function that converts a list of values, but combines the error message
     */
    def parseList(fn: A => Either[ErrorMessage, B]) : Either[ErrorMessage, Seq[B]] = {
      val results = s.zip(s.map(fn))
      val errors = results.collect {
        case (inputValue, Left(errorMessage)) => s"$inputValue -> errorMessage"
      }
      if (errors.isEmpty)
        Right(results.collect {
          case (_, Right(value)) => value
        })
      else
        Left(errors.mkString(System.lineSeparator()))
    }
  }
}
