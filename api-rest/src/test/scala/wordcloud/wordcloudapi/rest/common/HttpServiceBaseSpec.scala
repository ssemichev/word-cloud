package wordcloud.wordcloudapi.rest.common

import akka.event.NoLogging
import akka.http.scaladsl.testkit.ScalatestRouteTest
import wordcloud.testkit.UnitTestSpec

import scala.concurrent.Await
import scala.concurrent.duration.Duration

trait HttpServiceBaseSpec extends UnitTestSpec with ScalatestRouteTest {

  val logger = NoLogging

  val config = new AppConfig()

  def responseAsString: String = {
    // Need to import Predefined Unmarshallers for handling String
    import akka.http.scaladsl.unmarshalling.Unmarshaller._
    responseAs[String]
  }
}
