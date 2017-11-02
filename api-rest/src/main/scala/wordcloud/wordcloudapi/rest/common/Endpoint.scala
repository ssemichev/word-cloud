package wordcloud.wordcloudapi.rest.common

import java.lang.management.ManagementFactory

import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import wordcloud.common.{ BuildInfo, ToDateHelper, now }
import wordcloud.wordcloudapi.rest.common.models.{ About, HealthCheckResponse, Status }

import scala.concurrent.{ Await, ExecutionContext }
import scala.concurrent.duration.{ Duration, _ }
import scala.language.{ implicitConversions, postfixOps }
import scala.util.{ Failure, Success }

trait DefaultRoutes extends BaseServiceRoute with CustomLogging {
  protected def config: BaseConfig

  private[this] lazy val serviceName = config.serviceConfig.name
  private[this] lazy val serviceVersion = config.serviceConfig.version
  protected lazy val httpConfig = config.httpConfig

  protected val defaultRoutes: Route = {
    pathPrefix("status") {
      pathEndOrSingleSlash {
        get {
          val uptime = Duration(ManagementFactory.getRuntimeMXBean.getUptime, MILLISECONDS).toString()
          complete(Status(serviceName, uptime))
        }
      }
    } ~
      pathPrefix("about") {
        pathEndOrSingleSlash {
          get {
            complete(About(serviceName, now.toUtcIso, BuildInfo.version, BuildInfo.builtAtString))
          }
        }
      } ~
      pathPrefix("health") {
        pathEndOrSingleSlash {
          get {
            complete(HealthCheckResponse(true))
          }
        }
      }
  }

  protected def routes(serviceRoutes: Route)(implicit m: Materializer, ex: ExecutionContext): Route = {
    requestMethodAndResponseStatusAsInfo(Logging.InfoLevel) {
      pathPrefix(serviceVersion) {
        logRequestResult(serviceName, Logging.DebugLevel) {
          defaultRoutes
        } ~
          logRequestResult(serviceName, Logging.InfoLevel) {
            serviceRoutes
          }
      } ~
        path("")(getFromResource("public/index.html"))
    }
  }

}

trait Endpoint extends System with DefaultRoutes {

  def publish(): Unit = {

    val (host, port) = (httpConfig.interface, httpConfig.port)
    val serverBinding = Http().bindAndHandle(routes(serviceRoutes), host, port)

    scala.sys.addShutdownHook {
      serverBinding.flatMap(_.unbind())
      system.terminate()
      logger.info(s"${system.name} has been terminated")
      Await.result(system.whenTerminated, 1 minute)
    }

    serverBinding.onComplete {
      case Success(_)  => logger.info("Has been running on {}:{}...", host, port)
      case Failure(ex) => logger.error(ex, "Failed to bind to {}:{}!", host, port)
    }
  }

  protected def serviceRoutes: Route

}

