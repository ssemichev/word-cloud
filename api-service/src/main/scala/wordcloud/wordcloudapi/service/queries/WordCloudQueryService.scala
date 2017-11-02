package wordcloud.wordcloudapi.service.queries

import javax.inject.Inject

import akka.actor.{ Actor, ActorLogging }
import akka.pattern.pipe
import wordcloud.common.traits.NamedActor
import wordcloud.wordcloudapi.service.models.{ TopWordsResponse, WordCloudSearchCriteria }

import scala.concurrent.{ ExecutionContext, Future }

object WordCloudQueryService extends NamedActor {

  override final val name = "word-cloud-query-service"

  case class FindTopWordsQuery(criteria: WordCloudSearchCriteria)
}

class WordCloudQueryService @Inject() ()(implicit ec: ExecutionContext) extends Actor with ActorLogging {

  import WordCloudQueryService._

  log.debug(s"JobQueryService is starting... - ${self.path.name}")

  def receive: Receive = {
    case FindTopWordsQuery(criteria) => findBy(criteria) pipeTo sender
  }

  def findBy(criteria: WordCloudSearchCriteria): Future[TopWordsResponse] = {
    Future.successful {
      TopWordsResponse(words = Map("a" -> 10, "b" -> 20))
    }
  }
}
