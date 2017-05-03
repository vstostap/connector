package connector

import connector.api.API
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{ FlatSpec, Matchers }


class APISpec extends FlatSpec with API with Matchers with ScalatestRouteTest {

  "Connector API" should
    "return a title for GET request to the root path" in {
      Get() ~> routes ~> check {
        status shouldBe StatusCodes.OK
        responseAs[String] must contain("Connector")
      }
    }
}