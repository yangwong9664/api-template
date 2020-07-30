package services

import fixture.TestFixture
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.time.{Seconds, Span}
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, Matchers, WordSpec}

class TemplateSpec extends WordSpec with Matchers with TestFixture with ScalaFutures
  with IntegrationPatience with BeforeAndAfterEach with BeforeAndAfterAll{

  implicit val defaultPatience: PatienceConfig = PatienceConfig(timeout = Span(TIME_OUT, Seconds), interval = Span(INTERVAL, Seconds))

  override protected def beforeEach(): Unit = {
    super.beforeEach()
  }

  override protected def afterAll(): Unit = {
    super.afterAll()
  }

}
