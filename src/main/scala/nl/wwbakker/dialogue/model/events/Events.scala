package nl.wwbakker.dialogue.model.events

import nl.wwbakker.dialogue.model.relation.{Relation, RelationId}
import nl.wwbakker.dialogue.model.{Assertion, AssertionId}

sealed trait Event

// Validation: 
// - Assertion needs 1 initial assertion, unless it's the first assertion.
case class AssertionAdded(assertion: Assertion)

// Validation:
// - fromAssertion exists
// - relation.relatedToAssertion exists
case class RelationAdded(fromAssertion: AssertionId, 
                         relation: Relation)

// Validation:
// - relationId exists
case class RelationInvalidated(relationId: RelationId)

// Validation:
// - supercededAssertion exists
case class AssertionSuperceded(supercededAssertion: AssertionId, 
                               newAssertion: Assertion)

// Result of invalidates relation (geen actie)
case class AssertionInvalidated(invalidatedAssertion: AssertionId)
