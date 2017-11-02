package wordcloud.wordcloudapi.service.queries

import javax.inject.{ Inject, Named }

import akka.actor.{ Actor, ActorLogging, ActorRef }
import akka.pattern.pipe
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._
import wordcloud.common.traits.NamedActor
import wordcloud.services.QueueService
import wordcloud.services.QueueService.{ Insert, Status }
import wordcloud.wordcloudapi.service.models.{ TopWordsResponse, WordCloudSearchCriteria }

import scala.concurrent.{ ExecutionContext, Future }
import scala.language.postfixOps

object WordCloudQueryService extends NamedActor {

  override final val name = "word-cloud-query-service"

  case class FindTopWordsQuery(criteria: WordCloudSearchCriteria)

  case class PostUrl(url: String)

  case object GetQueueStatus

}

class WordCloudQueryService @Inject()(@Named(QueueService.name) queueService: ActorRef)(implicit ec: ExecutionContext) extends Actor with ActorLogging {

  import WordCloudQueryService._

  implicit val timeout = Timeout(5 seconds)

  log.debug(s"JobQueryService is starting... - ${self.path.name}")

  def receive: Receive = {
    case FindTopWordsQuery(criteria) => findBy(criteria) pipeTo sender
    case PostUrl(url) => queueService ? Insert(url) pipeTo sender
    case GetQueueStatus => queueService ? Status pipeTo sender
  }

  def findBy(criteria: WordCloudSearchCriteria): Future[TopWordsResponse] = {
    Future.successful {
      TopWordsResponse(words = Map("a" -> 10, "b" -> 20))
    }
  }
}
