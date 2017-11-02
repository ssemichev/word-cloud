package wordcloud.services

import javax.inject.Inject

import akka.pattern.pipe
import akka.actor.{ Actor, ActorLogging }
import wordcloud.common.traits.NamedActor

import scala.concurrent.{ ExecutionContext, Future }

object QueueService extends NamedActor {

  override final val name = "queue-service"

  case class Insert(url: String)

  case object Status
}

class QueueService @Inject() ()(implicit ec: ExecutionContext) extends Actor with ActorLogging {

  import QueueService._

  log.debug(s"QueueService is starting... - ${self.path.name}")

  var urls_topic: Vector[String] = Vector.empty

  def receive: Receive = {
    case Insert(url) => {
      insert(url) pipeTo sender
    }
    case Status => Future.successful { urls_topic.size.toString } pipeTo sender
  }

  def insert(url: String): Future[String] = {
    urls_topic = urls_topic :+ url
    log.debug(s"Insert item... - ${urls_topic.size}")
    Future.successful {
      url
    }
  }
}
