package nl.wwbakker.dialogue.model.ids

import play.api.libs.json._

trait Id {
  def value : Int
}

class IdFormatter[A <: Id](prefix : String, ctr : Int => A) extends Format[A] {
  override def reads(json: JsValue): JsResult[A] = json match {
    case JsString(s) if s.startsWith(prefix) && s.substring(1).toIntOption.isDefined =>
      JsSuccess(ctr(s.substring(1).toInt))
    case _ => JsError(s"Cannot parse Id: It is not in the correct format. Expected $prefix[number].")
  }
  override def writes(o: A): JsValue = JsString(prefix + o.value)
}