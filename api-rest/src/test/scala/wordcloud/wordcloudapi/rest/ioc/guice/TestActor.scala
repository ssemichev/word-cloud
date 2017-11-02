package wordcloud.wordcloudapi.rest.ioc.guice

import akka.actor.Actor
import wordcloud.common.traits.NamedActor

object TestActor extends NamedActor {

  override final val name = "test-actor"

  case object GetStatus
  case object GetDispatcherName
}

class TestActor() extends Actor {

  import TestActor._

  override def receive: Receive = {
    case GetStatus         => sender ! "green"
    case GetDispatcherName => sender ! context.dispatcher.toString
  }
}
