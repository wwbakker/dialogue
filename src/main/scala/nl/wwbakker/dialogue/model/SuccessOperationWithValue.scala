package nl.wwbakker.dialogue.model

trait SuccessOperation {
  def alwaysText : String
}

object SuccessOperationWithoutMessage extends SuccessOperation {
  def alwaysText : String = "success"
}
case class SuccessOperationWithMessage(text: String) extends SuccessOperation {
  override def alwaysText: String = text
}

