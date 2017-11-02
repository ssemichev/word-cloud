package wordcloud.wordcloudapi.rest.ioc

import javax.inject.Inject

import akka.actor.ActorSystem
import akka.event.{ Logging, LoggingAdapter }
import akka.stream.ActorMaterializer
import com.google.inject.{ PrivateModule, Provides, Singleton }
import wordcloud.wordcloudapi.rest.common.AppConfig
import net.codingwell.scalaguice.ScalaPrivateModule

import scala.concurrent.ExecutionContext

private[wordcloud] class AkkaModule extends PrivateModule with ScalaPrivateModule {

  override def configure(): Unit = {
    bindActorSystem()

    expose[ActorSystem]
    expose[ActorMaterializer]
    expose[ExecutionContext]
    expose[LoggingAdapter]
  }

  private[this] def bindActorSystem() = {
    bind[ActorSystem].toProvider[ActorSystemProvider].asEagerSingleton()
  }

  @Provides
  @Singleton
  def provideActorMaterializer(@Inject() system: ActorSystem): ActorMaterializer = ActorMaterializer.create(system)

  @Provides
  @Singleton
  def provideExecutionContextExecutor(@Inject() system: ActorSystem): ExecutionContext = system.dispatcher

  @Provides
  @Singleton
  def provideLoggingAdapter(@Inject() system: ActorSystem, config: AppConfig): LoggingAdapter = Logging(system, config.serviceConfig.name)
}