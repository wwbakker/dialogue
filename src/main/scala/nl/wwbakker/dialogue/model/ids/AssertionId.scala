package nl.wwbakker.dialogue.model.ids

import play.api.libs.json.Format

case class AssertionId(value: Int) extends Id
object AssertionId {
  def prefix = "A"
  implicit def format : Format[AssertionId] = new IdFormatter[AssertionId](AssertionId.prefix, AssertionId.apply)
}