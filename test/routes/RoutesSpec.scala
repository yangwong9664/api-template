package routes

import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.google.inject.{Guice, Injector}
import module.GuiceModule
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpec}

class RoutesSpec extends WordSpec with Matchers with ScalatestRouteTest with MockFactory  {

  val injector: Injector = Guice.createInjector(new GuiceModule)
  val serviceRoutes: Routes = injector.getInstance(classOf[Routes])

  "/hello" should {

    "return a 200 when a successful" in {
      HttpRequest(HttpMethods.GET, uri = "/hello") ~> serviceRoutes.routes ~> check {
        response.status shouldBe StatusCodes.OK
      }
    }
  }

}
