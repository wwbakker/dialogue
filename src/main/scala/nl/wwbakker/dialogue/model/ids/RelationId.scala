package nl.wwbakker.dialogue.model.ids

import play.api.libs.json.Format

case class RelationId(value : Int) extends Id {
  override def toString: String = RelationId.prefix + value
}
object RelationId {
  def prefix = "R"
  implicit def format : Format[RelationId] = new IdFormatter[RelationId](RelationId.prefix, RelationId.apply)
}