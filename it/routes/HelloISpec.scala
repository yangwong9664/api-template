package routes

import helpers.IntegrationSpecBase
import org.scalatest.WordSpec
import org.scalatest.time.{Millis, Seconds, Span}

import scala.util.Random

class HelloISpec extends WordSpec with IntegrationSpecBase {

  implicit val defaultPatience: PatienceConfig = PatienceConfig(timeout = Span(5, Seconds), interval = Span(100, Millis))
  override val port: Int = 2000 + Random.nextInt(999)

  "hello" should {
    "return 200 given a success" in {

      val res = buildGetClient(s"http://localhost:$port/hello")
      whenReady(res) { result =>
        result.discardEntityBytes()
        result.status.intValue() shouldBe 200
      }
    }
  }


}
