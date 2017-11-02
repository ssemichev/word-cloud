package wordcloud.wordcloudapi.rest.http

import akka.http.scaladsl.server.Route
import com.google.inject.Inject
import wordcloud.wordcloudapi.rest.common.HttpService
import wordcloud.wordcloudapi.rest.http.routes.WordCloudQueryServiceRoute

class WordCloudQueryHttpService @Inject() (val wordCloudRouter: WordCloudQueryServiceRoute) extends HttpService {

  val routes: Route = wordCloudRouter.route
}