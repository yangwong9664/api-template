package app

import java.net.InetAddress
import utils.logging.Log._
import akka.http.scaladsl.Http
import com.google.inject.{ Guice, Injector }
import module.GuiceModule
import routes.Routes
import utils.AppSystem

trait MainApp extends AppSystem {

  lazy val injector: Injector = Guice.createInjector(new GuiceModule)

  lazy val appConfig: AppConfig = injector.getInstance(classOf[AppConfig])

  lazy val serviceRoutes: Routes = injector.getInstance(classOf[Routes])

  lazy val httpPort: Int = appConfig.httpPort

  def main(args: Array[String]): Unit = {
    Http().bindAndHandle(serviceRoutes.routes, appConfig.httpHost, httpPort)
    logger.info(logMessage("Application Running", s"${appConfig.appName} running at ${appConfig.httpHost}:$httpPort on ${InetAddress.getLocalHost}"))
  }
}

object Main extends MainApp
