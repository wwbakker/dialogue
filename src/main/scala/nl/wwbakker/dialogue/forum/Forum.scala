package nl.wwbakker.dialogue.forum

import nl.wwbakker.dialogue.model.Assertion
import nl.wwbakker.dialogue.model.events.{AssertionAdded, AssertionSuperseded, Event}
import nl.wwbakker.dialogue.model.ids.{AssertionId, RelationId}
import nl.wwbakker.dialogue.model.relation.{RelationType, Supersedes}

class Forum(initialEvents : Seq[Event]) {
  var assertions : Seq[Assertion] = initialEvents.flatMap {
    case AssertionAdded(assertion) => Some(assertion)
    case AssertionSuperseded(_, newAssertion, _) => Some(newAssertion)
    case _ => None
  }
  def topic : Option[Assertion] = assertions.headOption

  def topicWithHistory : (Seq[Assertion], Option[Assertion]) = {
    def topicWithHistoryStep(fromAssertion : Assertion) : List[Assertion] = {
      relatedToOfType(fromAssertion.id, Supersedes).headOption match {
        case None => fromAssertion :: Nil
        case Some(value) => fromAssertion :: topicWithHistoryStep(value)
      }
    }
    assertions.headOption.map(topicWithHistoryStep).getOrElse(Nil).reverse match {
      case currentTopic :: reversedHistory =>
        (reversedHistory.reverse, Some(currentTopic))
      case Nil =>
        (Nil, None)
    }
  }

  def topicWithHistoryFormatted : String = {
    val (history, currentTopic) = topicWithHistory
    currentTopic match {
      case Some(topic) =>
        s"topic:${System.lineSeparator()}" +
          history.map(_.idAndName).map(s => s"~~$s~~").mkString(System.lineSeparator()) +
          System.lineSeparator() +
          topic.idAndName +
          relatedFormatted(topic.id)
      case None =>
        "no topic"
    }
  }

  def relatedFormatted(relatedToAssertionId : AssertionId) : String =
    RelationType.all.map(relatedOfTypeFormatted(relatedToAssertionId, _)).mkString("\n")

  def relatedOfTypeFormatted(relatedToAssertionId : AssertionId, relationType : RelationType) : String = {
    val r = relatedToOfType(relatedToAssertionId, relationType)
    r.map(_.idAndName).map(s => s"${relationType.name} ${relatedToAssertionId}: $s").mkString(System.lineSeparator())
  }

  private def relatedToOfType(relatedToAssertionId : AssertionId, relationType : RelationType) : Seq[Assertion] =
    assertions.filter(assertion =>
      assertion.relatesTo.exists(r => r.relatedToAssertion == relatedToAssertionId &&
        r.relationType == relationType))

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
