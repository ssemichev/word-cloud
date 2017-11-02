package wordcloud.wordcloudapi.rest.ioc

import javax.inject.Inject

import akka.actor.ActorSystem
import com.google.inject.{ Injector, Provider }
import wordcloud.wordcloudapi.rest.common.AppConfig
import wordcloud.wordcloudapi.rest.ioc.guice.GuiceAkkaExtension

class ActorSystemProvider @Inject() (val config: AppConfig, val injector: Injector) extends Provider[ActorSystem] {

  override def get(): ActorSystem = {
    val system = ActorSystem(s"${config.serviceConfig.name}-actor-system", config.config)
    GuiceAkkaExtension(system).initialize(injector)
    system
  }
}
