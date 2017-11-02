package wordcloud.wordcloudapi.rest

import javax.inject.Named

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.google.inject.Inject
import wordcloud.wordcloudapi.rest.common.authentication.UserPassAuthenticator
import wordcloud.wordcloudapi.rest.common.{ AppConfig, Endpoint }
import wordcloud.wordcloudapi.rest.http.WordCloudQueryHttpService

import scala.concurrent.ExecutionContext

class JobQueryEndpoint @Inject() (
    protected val config:                                AppConfig,
    implicit val system:                                 ActorSystem,
    implicit val materializer:                           ActorMaterializer,
    implicit val executor:                               ExecutionContext,
    implicit val logger:                                 LoggingAdapter,
    httpService:                                         WordCloudQueryHttpService,
    @Named("commandEndpoint") private val authenticator: UserPassAuthenticator[String]
) extends Endpoint {

  override protected val serviceRoutes: Route = {
    authenticateBasic(realm = "Secure command endpoint", authenticator.authenticate) { userName =>
      httpService.routes
    }
  }
}