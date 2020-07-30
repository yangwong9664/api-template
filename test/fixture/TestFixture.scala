package fixture

import akka.http.scaladsl.model._
import app.ApplicationConfig
import cats.data.EitherT
import cats.implicits._
import errors.AppErrors
import org.scalamock.scalatest.MockFactory
import routes.Routes
import services._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait TestFixture extends MockFactory {

  val TIME_OUT = 5
  val INTERVAL = 0.1

  val appConfig = new ApplicationConfig

  def routes: Routes = new Routes(appConfig)

  def createResultType[T](model: T): Result[T] = EitherT.right(Future(model))

  def createResultTypeChazError[T](error: AppErrors): Result[T] = EitherT.left(Future(error))

  def createResultException[T]: Result[T] = EitherT.right(Future.failed(new Exception("error")))

}
