package helpers

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import org.scalatest.Suite
import org.scalatest.concurrent.{Eventually, IntegrationPatience}

import scala.concurrent.Future

trait WireMockConfig {

  val wiremockPort = 11111
  val wiremockHost = "localhost"
  val url = s"http://$wiremockHost:$wiremockPort"
}

trait WiremockHelper extends Eventually with IntegrationPatience with WireMockConfig {
  self: Suite =>

  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()

  lazy val wmConfig = wireMockConfig().port(wiremockPort)
  lazy val wireMockServer = new WireMockServer(wmConfig)

  def buildPostClient(path: String, body: String): Future[HttpResponse] =
    Http().singleRequest(
      HttpRequest(
        HttpMethods.POST,
        uri = path,
        entity = HttpEntity(ContentTypes.`application/json`, body))
    )

  def buildPutClient(path: String, body: String): Future[HttpResponse] =
    Http().singleRequest(
      HttpRequest(
        HttpMethods.PUT,
        uri = path,
        entity = HttpEntity(ContentTypes.`application/json`, body))
    )

  def buildGetClient(path: String): Future[HttpResponse] =
    Http().singleRequest(
      HttpRequest(
        HttpMethods.GET,
        uri = path)
    )

  def buildPatchClient(path: String): Future[HttpResponse] =
    Http().singleRequest(
      HttpRequest(
        HttpMethods.PATCH,
        uri = path)
    )

  def buildDeleteClient(path: String): Future[HttpResponse] =
    Http().singleRequest(
      HttpRequest(
        HttpMethods.DELETE,
        uri = path
      )
    )


  def stubPost(url: String, status: StatusCode, responseBody: String): StubMapping =
    stubFor(post(urlMatching(url))
      .willReturn(
        aResponse().
          withStatus(status.intValue())
          .withHeader("Content-Type", "application/json")
          .withBody(responseBody)
      )
    )

  def stubPost(url: String, status: StatusCode): StubMapping =
    stubFor(post(urlMatching(url))
      .willReturn(
        aResponse().
          withStatus(status.intValue())
      )
    )

  def stubGet(url: String, status: StatusCode, responseBody: String): StubMapping =
    stubFor(get(urlMatching(url))
      .willReturn(
        aResponse().
          withStatus(status.intValue())
          .withHeader("Content-Type", "application/json")
          .withBody(responseBody)
      )
    )

  def stubGet(url: String, status: StatusCode): StubMapping =
    stubFor(get(urlMatching(url))
      .willReturn(
        aResponse().
          withStatus(status.intValue())
      )
    )

  def stubDelete(url: String, status: StatusCode): StubMapping =
    stubFor(delete(urlMatching(url))
      .willReturn(
        aResponse().
          withStatus(status.intValue())
      )
    )

  def stubDelete(url: String, status: StatusCode,  responseBody: String): StubMapping =
    stubFor(delete(urlMatching(url))
      .willReturn(
        aResponse().
          withStatus(status.intValue())
          .withHeader("Content-Type", "application/json")
          .withBody(responseBody)
      )
    )

  def startWiremock(): Unit = {
    wireMockServer.start()
    WireMock.configureFor(wiremockHost, wiremockPort)
  }

  def stopWiremock(): Unit = {
    wireMockServer.stop()
  }

  def resetWiremock(): Unit = WireMock.reset()
}
