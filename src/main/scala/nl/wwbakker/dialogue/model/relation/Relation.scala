package nl.wwbakker.dialogue.model.relation

import nl.wwbakker.dialogue.model.AssertionId
import nl.wwbakker.dialogue.model.relation.RelationType
import java.util.UUID

opaque type RelationId = UUID

case class Relation(relationId: RelationId, 
                    relatedToAssertion: AssertionId, 
                    relationType: RelationType)