package wordcloud.wordcloudapi.rest.ioc

import javax.inject.{ Inject, Named }

import akka.actor.{ Actor, ActorRef, ActorSystem }
import com.google.inject.name.Names
import com.google.inject.{ PrivateModule, Provides, Singleton }
import wordcloud.wordcloudapi.rest.http.WordCloudQueryHttpService
import wordcloud.wordcloudapi.rest.ioc.guice.GuiceAkkaActorRefProvider
import wordcloud.wordcloudapi.service.queries.WordCloudQueryService
import net.codingwell.scalaguice.ScalaPrivateModule
import wordcloud.services.QueueService
import wordcloud.wordcloudapi.rest.http.routes.WordCloudQueryServiceRoute

private[wordcloud] class ServicesModule extends PrivateModule with ScalaPrivateModule with GuiceAkkaActorRefProvider {

  override def configure(): Unit = {
    installModules()
    setupServices()

    expose[Actor].annotatedWith(Names.named(QueueService.name))
    expose[Actor].annotatedWith(Names.named(WordCloudQueryService.name))
    expose[WordCloudQueryHttpService]
  }

  private[this] def installModules() = {

  }

  private[this] def setupServices(): Unit = {

    bind[Actor].annotatedWithName(QueueService.name).to[QueueService]

    bind[Actor].annotatedWithName(WordCloudQueryService.name).to[WordCloudQueryService]

    bind[WordCloudQueryServiceRoute].in[Singleton]
    bind[WordCloudQueryHttpService].in[Singleton]
  }

  @Provides
  @Singleton
  @Named(QueueService.name)
  def provideQueueServiceRef(@Inject() system: ActorSystem): ActorRef = {
    provideActorRef(system, QueueService.name, Some(QueueService.name))
  }

  @Provides
  @Singleton
  @Named(WordCloudQueryService.name)
  def provideJobQueryServiceRef(@Inject() system: ActorSystem): ActorRef = {
    provideActorRef(system, WordCloudQueryService.name, Some(WordCloudQueryService.name))
  }

  //  @Provides
  //  @Named("actorWithDispatcher")
  //  def provideActorWithDispatcher(@Inject() system: ActorSystem): ActorRef = {
  //    system.actorOf(Props[Actor].withDispatcher("my-dispatcher"))
  //  }

  //  @Provides
  //  @Named("actorWithDispatcher")
  //  def provideActorWithDispatcher1(@Inject() system: ActorSystem): ActorRef = {
  //    system.actorOf(Props[JobQueryService].withDispatcher("my-dispatcher"))
  //  }
}
