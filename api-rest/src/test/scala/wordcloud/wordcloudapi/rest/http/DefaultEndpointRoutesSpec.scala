package wordcloud.wordcloudapi.rest.http

import akka.http.scaladsl.model.{ ContentTypes, StatusCodes }
import akka.http.scaladsl.server.{ Directive, Route }
import wordcloud.wordcloudapi.rest.common.models.{ About, HealthCheckResponse, Status }
import wordcloud.wordcloudapi.rest.common.{ AppConfig, BaseConfig, DefaultRoutes, HttpServiceBaseSpec }

class DefaultEndpointRoutesSpec extends HttpServiceBaseSpec {

  trait Context extends DefaultRoutes {
    override def config: BaseConfig = new AppConfig()
  }

  behavior of "Default routes"

  it should "respond to Health query" in new Context {
    Get("/health") ~> defaultRoutes ~> check {
      status shouldBe StatusCodes.OK
      contentType shouldBe ContentTypes.`application/json`
      responseAs[HealthCheckResponse] shouldBe HealthCheckResponse(true)
    }
  }

  it should "respond to About query" in new Context {
    Get("/about") ~> defaultRoutes ~> check {
      status shouldBe StatusCodes.OK
      contentType shouldBe ContentTypes.`application/json`
      responseAs[About].serviceName shouldBe config.serviceConfig.name
    }
  }

  it should "respond to Status query" in new Context {
    Get("/status") ~> defaultRoutes ~> check {
      status shouldBe StatusCodes.OK
      contentType shouldBe ContentTypes.`application/json`
      responseAs[Status].serviceName shouldBe config.serviceConfig.name
      responseAs[Status].uptime should include("milliseconds")
    }
  }

  it should "return a NotFound error for PUT requests to a bad url" in new Context {
    Get("/bad-url") ~> Route.seal(defaultRoutes) ~> check {
      status shouldBe StatusCodes.NotFound
      responseAsString shouldEqual "The requested resource could not be found."
    }
  }

  it should "return a NotFound error for PUT requests to the root path" in new Context {
    Put("/bad-url") ~> Route.seal(defaultRoutes) ~> check {
      status shouldBe StatusCodes.NotFound
      responseAsString shouldEqual "The requested resource could not be found."
    }
  }

  it should "make About query to other paths handled" in new Context {
    Get("/about") ~> defaultRoutes ~> check {
      handled shouldBe true
    }
  }
}
