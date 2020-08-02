package nl.wwbakker.dialogue.model.relation

import nl.wwbakker.dialogue.model.ids.{AssertionId, RelationId}
import play.api.libs.json.{Json, OFormat}

case class Relation(relationId: RelationId,
                    relatedToAssertion: AssertionId,
                    relationType: RelationType)

object Relation {
  implicit val formatter: OFormat[Relation] = Json.format[Relation]
}