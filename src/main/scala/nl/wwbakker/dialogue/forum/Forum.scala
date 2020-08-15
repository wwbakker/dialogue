package nl.wwbakker.dialogue.forum

import nl.wwbakker.dialogue.model.Assertion
import nl.wwbakker.dialogue.model.events.{AssertionAdded, AssertionSuperseded, Event}
import nl.wwbakker.dialogue.model.ids.{AssertionId, RelationId}

class Forum(initialEvents : Seq[Event]) {
  var assertions : Seq[Assertion] = initialEvents.flatMap {
    case AssertionAdded(assertion) => Some(assertion)
    case AssertionSuperseded(_, newAssertion, _) => Some(newAssertion)
    case _ => None
  }
  def topic : Option[Assertion] = assertions.headOption

  private var previousAssertionId =
    assertions.lastOption
    .map(_.id.value).getOrElse(0)

  def generateAssertionId(): AssertionId = {
    previousAssertionId += 1
    AssertionId(previousAssertionId)
  }

  private var previousRelationId =
    assertions.lastOption.toList
      .flatMap(_.relatesTo)
      .foldLeft(0)((a, b) => Math.max(a, b.relationId.value))

  def generateRelationId(): RelationId = {
    previousRelationId += 1
    RelationId(previousRelationId)
  }
}
