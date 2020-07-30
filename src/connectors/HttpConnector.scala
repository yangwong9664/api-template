package connectors

import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import spray.json._
import utils.AppSystem

import scala.concurrent.Future

trait HttpConnector extends AppSystem {

  def singleRequest(request: HttpRequest): Future[HttpResponse] = Http().singleRequest(request)

  def httpGetRequest(uri: String): Future[HttpResponse] = {
    singleRequest(
      HttpRequest(
        method = HttpMethods.GET,
        uri = uri
      )
    )
  }

  def httpPostRequest(uri: String, body: JsValue): Future[HttpResponse] = {
    singleRequest(
      HttpRequest(
        method = HttpMethods.POST,
        uri = uri,
        entity = HttpEntity(ContentTypes.`application/json`, body.toString)
      )
    )
  }

  def httpPatchRequest(uri: String): Future[HttpResponse] = {
    singleRequest(
      HttpRequest(
        method = HttpMethods.PATCH,
        uri = uri
      )
    )
  }

  def httpDeleteRequest(uri: String): Future[HttpResponse] = {
    singleRequest(
      HttpRequest(
        method = HttpMethods.DELETE,
        uri = uri
      )
    )
  }

}
