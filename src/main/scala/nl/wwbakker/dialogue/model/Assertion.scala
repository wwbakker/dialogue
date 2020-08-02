package nl.wwbakker.dialogue.model

import java.util.UUID
import nl.wwbakker.dialogue.model.relation.Relation

opaque type AssertionId = UUID
case class Assertion(id : AssertionId, text: String, relatesTo: List[Relation])