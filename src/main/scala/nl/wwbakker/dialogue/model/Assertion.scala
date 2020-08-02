package nl.wwbakker.dialogue.model

import java.util.UUID

import nl.wwbakker.dialogue.model.relation.Relation
import play.api.libs.json.{Json, OFormat}

case class Assertion(id : UUID, text: String, relatesTo: Seq[Relation])

object Assertion {
  implicit val assertionFormat : OFormat[Assertion] = Json.format[Assertion]
}