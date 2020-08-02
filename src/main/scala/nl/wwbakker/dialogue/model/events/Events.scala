package nl.wwbakker.dialogue.model.events

import java.util.UUID

import nl.wwbakker.dialogue.model.Assertion
import nl.wwbakker.dialogue.model.relation.Relation
import play.api.libs.json.{Json, OFormat}

sealed trait Event

// Validation: 
// - Assertion needs 1 initial assertion, unless it's the first assertion.
case class AssertionAdded(assertion: Assertion) extends Event

// Validation:
// - fromAssertion exists
// - relation.relatedToAssertion exists
case class RelationAdded(fromAssertion: UUID,
                         relation: Relation) extends Event

// Validation:
// - relationId exists
case class RelationInvalidated(relationId: UUID) extends Event

// Validation:
// - supersededAssertion exists
case class AssertionSuperseded(supersededAssertion: UUID,
                               newAssertion: Assertion) extends Event

// Result of invalidates relation (geen actie)
case class AssertionInvalidated(invalidatedAssertion: UUID) extends Event

object Event {
  implicit val assertionAddedFormat: OFormat[AssertionAdded] = Json.format[AssertionAdded]
  implicit val relationAddedFormat: OFormat[RelationAdded] = Json.format[RelationAdded]
  implicit val relationInvalidatedFormat: OFormat[RelationInvalidated] = Json.format[RelationInvalidated]
  implicit val assertionSupersededFormat: OFormat[AssertionSuperseded] = Json.format[AssertionSuperseded]
  implicit val assertionInvalidatedFormat: OFormat[AssertionInvalidated] = Json.format[AssertionInvalidated]
  implicit val eventFormat : OFormat[Event] = Json.format[Event]
}