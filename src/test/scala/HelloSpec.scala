import nl.wwbakker.dialogue.model.Assertion
import nl.wwbakker.dialogue.model.events.AssertionAdded
import nl.wwbakker.dialogue.model.ids.{AssertionId, RelationId}
import nl.wwbakker.dialogue.model.relation.{Relation, Supersedes}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import play.api.libs.json.Json


class HelloSpec extends AnyFlatSpec with Matchers {
  private val relationId = RelationId.generateId()
  private val assertionId = AssertionId.generateId()

  "Relation" should "be formattable" in {
    Json.prettyPrint(Json.toJson(Relation(relationId, assertionId, Supersedes))) shouldEqual
      """{
        |  "relationId" : "R1",
        |  "relatedToAssertion" : "A1",
        |  "relationType" : "Supersedes"
        |}""".stripMargin
  }
  "Events" should "be formattable" in {
    Json.prettyPrint(Json.toJson(
      AssertionAdded(
        Assertion(
          id = assertionId,
          text = "Pudge zorgt ervoor dat teams verliezen",
          relatesTo = Nil)))) shouldEqual
      """{
        |  "assertion" : {
        |    "id" : "A1",
        |    "text" : "Pudge zorgt ervoor dat teams verliezen",
        |    "relatesTo" : [ ]
        |  }
        |}""".stripMargin
  }
}