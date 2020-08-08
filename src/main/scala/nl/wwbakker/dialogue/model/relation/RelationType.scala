package nl.wwbakker.dialogue.model.relation

import play.api.libs.json.{Format, JsError, JsResult, JsString, JsSuccess, JsValue, Json}

sealed trait RelationType
case object Supports extends RelationType
case object Invalidates extends RelationType
case object Detracts extends RelationType
case object Supersedes extends RelationType
case object Explains extends RelationType

object RelationType {
  type ErrorMessage = String

  implicit object RelationTypeFormatter extends Format[RelationType] {
    override def reads(json: JsValue): JsResult[RelationType] = json match {
      case JsString("Supports") => JsSuccess(Supports)
      case JsString("Invalidates") => JsSuccess(Invalidates)
      case JsString("Detracts") => JsSuccess(Detracts)
      case JsString("Supersedes") => JsSuccess(Supersedes)
      case JsString("Explains") => JsSuccess(Explains)
      case e => JsError(s"Cannot parse RelationType: '$e' is not a correct value.")
    }
    override def writes(o: RelationType): JsValue = o match {
      case Supports => JsString("Supports")
      case Invalidates =>JsString("Invalidates")
      case Detracts => JsString("Detracts")
      case Supersedes => JsString("Supersedes")
      case Explains => JsString("Explains")
    }
  }

  def parse(s : String) : Either[ErrorMessage, RelationType] = s.toLowerCase match {
    case "supports" => Right(Supports)
    case "invalidates" => Right(Invalidates)
    case "detracts" => Right(Detracts)
    case "supersedes" => Right(Supersedes)
    case "explains" => Right(Explains)
    case e => Left(s"Cannot parse RelationType: '$e' is not a correct value.")
  }
}