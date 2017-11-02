package wordcloud.wordcloudapi.rest.http.routes

import javax.inject.{ Inject, Named }

import akka.actor.ActorRef
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout
import wordcloud.common.traits.Translator
import wordcloud.wordcloudapi.rest.common.BaseServiceRoute
import wordcloud.wordcloudapi.rest.datacontracts.{ WordCloudSearchRequest, WordCloudSearchResponse }
import wordcloud.wordcloudapi.service.models.{ TopWordsResponse, WordCloudSearchCriteria }
import wordcloud.wordcloudapi.service.queries.WordCloudQueryService

import scala.concurrent.duration._
import scala.language.postfixOps

object WordCloudQueryServiceRoute {

  implicit val toWordCloudSearchResponse = new Translator[TopWordsResponse, WordCloudSearchResponse] {
    def translate(from: TopWordsResponse): WordCloudSearchResponse = {
      WordCloudSearchResponse(
        words = from.words
      )
    }
  }

  implicit val toWordCloudSearchCriteria = new Translator[WordCloudSearchRequest, WordCloudSearchCriteria] {
    def translate(from: WordCloudSearchRequest): WordCloudSearchCriteria = {
      WordCloudSearchCriteria(
        top = from.top
      )
    }
  }
}

class WordCloudQueryServiceRoute @Inject() (@Named(WordCloudQueryService.name) wordCloudQueryService: ActorRef) extends BaseServiceRoute {

  import WordCloudQueryService._
  import WordCloudQueryServiceRoute._

  implicit val timeout = Timeout(5 seconds)

  val route: Route =

    pathPrefix("search") {
      pathEndOrSingleSlash {
        get {
          parameters('top.as[Int].?) { (top) =>
            val request = WordCloudSearchRequest(top)
            doSearch(request)
          }
        }
      }
    }

  private[this] def doSearch(request: WordCloudSearchRequest)(implicit
    requestTranslator: Translator[WordCloudSearchRequest, WordCloudSearchCriteria],
                                                              responseTranslator: Translator[TopWordsResponse, WordCloudSearchResponse]): Route = {
    onSuccess((wordCloudQueryService ? FindTopWordsQuery(requestTranslator.translate(request))).mapTo[TopWordsResponse]) {
      case response if response.words.nonEmpty => respond(response)(responseTranslator)
      case _                                   => respond(StatusCodes.NoContent)
    }
  }
}

