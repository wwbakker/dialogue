package nl.wwbakker.dialogue.model.ids

import play.api.libs.json.Format

case class AssertionId(value: Int) extends Id
object AssertionId {
  def prefix = "A"
  private var previousId = 0
  def generateId(): AssertionId = {
    previousId += 1
    AssertionId(previousId)
  }

  implicit def format : Format[AssertionId] = new IdFormatter[AssertionId](AssertionId.prefix, AssertionId.apply)
}