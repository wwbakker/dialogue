package nl.wwbakker.dialogue.model.ids

import play.api.libs.json.Format

case class RelationId(value : Int) extends Id
object RelationId {
  def prefix = "R"
  private var previousId = 0
  def generateId(): RelationId = {
    previousId += 1
    RelationId(previousId)
  }

  implicit def format : Format[RelationId] = new IdFormatter[RelationId](RelationId.prefix, RelationId.apply)
}