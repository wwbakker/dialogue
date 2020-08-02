package nl.wwbakker.dialogue.forum

import nl.wwbakker.dialogue.model.Assertion

class Forum {
  var assertions : Seq[Assertion] = Nil
  def topic : Option[Assertion] = assertions.headOption
}
