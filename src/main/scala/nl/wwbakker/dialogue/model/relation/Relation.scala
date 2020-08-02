package nl.wwbakker.dialogue.model.relation

import java.util.UUID

import play.api.libs.json.{Json, OFormat}

case class Relation(relationId: UUID, 
                    relatedToAssertion: UUID,
                    relationType: RelationType)

object Relation {
  implicit val formatter: OFormat[Relation] = Json.format[Relation]
}