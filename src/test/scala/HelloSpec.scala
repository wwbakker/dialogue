import java.util.UUID

import nl.wwbakker.dialogue.model.relation.{Relation, Supersedes}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import play.api.libs.json.Json


class HelloSpec extends AnyFlatSpec with Matchers {
  private val uuid = UUID.fromString("750f36c9-c490-4a9c-8757-492d3d102db0")
  "Relation" should "be formattable" in {
    Json.prettyPrint(Json.toJson(Relation(uuid, uuid, Supersedes))) shouldEqual """{
      |  "relationId" : "750f36c9-c490-4a9c-8757-492d3d102db0",
      |  "relatedToAssertion" : "750f36c9-c490-4a9c-8757-492d3d102db0",
      |  "relationType" : "Supersedes"
      |}""".stripMargin
  }
  "Events" should "be formattable" in {

  }
}