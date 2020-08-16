package nl.wwbakker.dialogue.model

import nl.wwbakker.dialogue.model.ids.AssertionId
import nl.wwbakker.dialogue.model.relation.Relation
import play.api.libs.json.{Json, OFormat}

case class Assertion(id : AssertionId, text: String, relatesTo: Seq[Relation]) {
  def idAndName = s"$text [$id]"
}

object Assertion {
  implicit val assertionFormat : OFormat[Assertion] = Json.format[Assertion]
}