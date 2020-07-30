package helpers

import app.{AppConfig, MainApp, ApplicationConfig}
import com.github.tomakehurst.wiremock.client.WireMock.setGlobalFixedDelay
import com.google.inject.{Guice, Injector}
import com.typesafe.config.{Config, ConfigFactory}
import module.GuiceModule
import org.scalatest._
import org.scalatest.concurrent.{Eventually, IntegrationPatience, ScalaFutures}

import scala.concurrent.duration.FiniteDuration

trait IntegrationSpecBase extends WordSpec
  with GivenWhenThen with TestSuite with ScalaFutures with IntegrationPatience with Matchers
  with WiremockHelper with BeforeAndAfterEach with BeforeAndAfterAll with Eventually {

  val port: Int

  lazy val testInjector: Injector = Guice.createInjector(new GuiceModule {
    override def configure(): Unit = {
      bind(classOf[AppConfig]) to classOf[WiremockAppConfig]
    }
  })

  lazy val mongoDB: MongoDB = new MongoDB(new WiremockAppConfig)

  implicit lazy val app = new MainApp {
    override lazy val injector: Injector = testInjector
    override lazy val httpPort: Int = port
    override lazy val metricsEnabled: Boolean = enableMetrics
  }

  def statusOf(res: Future[HttpResponse])(implicit timeout: Duration): Int = Await.result(res, timeout).status.intValue()

  override def beforeEach(): Unit = {
    Await.result(mongoDB.metadataCollection.deleteMany(BsonDocument()).toFuture(), 20.seconds)
    resetWiremock()
  }

  override def beforeAll(): Unit = {
    super.beforeAll()
    app.main(Array.empty)
    startWiremock()
  }

  override def afterAll(): Unit = {
    Await.result(mongoDB.metadataCollection.deleteMany(BsonDocument()).toFuture(), 20.seconds)
    stopWiremock()
    super.afterAll()

  }

}
class WiremockAppConfig extends ApplicationConfig with WireMockConfig {
  override lazy val config: Config = ConfigFactory.parseString(
    s"""
       app.name = "yang-backend-template"

 http {
         host = "0.0.0.0"
         port = 8000
       }

 akka {
         loglevel = "INFO"
         loggers = ["akka.event.slf4j.Slf4jLogger"]
       }

 mongo-async-driver {
         akka {
           log-dead-letters-during-shutdown = off
           log-dead-letters = 0
         }
       }

 akka.http {
         server {
           server-header = YANG
         }
       }

 mongodb {
         database = yang-backend-template
         servers = ["mongo.private:27017"]
         user = "admin"
         password = "admin"
         authEnabled = false
       }
       
     """.stripMargin)

  override lazy val httpPort: Int = 2222

}
