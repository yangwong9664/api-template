package routes

import javax.inject.Inject
import javax.inject.Singleton

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives.{ path, _ }
import akka.http.scaladsl.server._
import app.AppConfig
import utils.AppSystem

@Singleton
class Routes @Inject() (appConfig: AppConfig) extends AppSystem {

  lazy val routes: Route = {
    path("hello") {
      get {
        complete(OK)
      }
    }
  }

}
