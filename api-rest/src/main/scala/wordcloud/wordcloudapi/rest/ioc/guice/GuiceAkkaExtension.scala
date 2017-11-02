package wordcloud.wordcloudapi.rest.ioc.guice

import akka.actor.{ ActorRef, ActorSystem, ExtendedActorSystem, Extension, ExtensionId, ExtensionIdProvider, Props }
import com.google.inject.Injector

/**
 * An Akka extension implementation for Guice-based injection. The Extension provides Akka access to
 * dependencies defined in Guice.
 */
class GuiceAkkaExtensionImpl extends Extension {

  private var injector: Injector = _

  def initialize(injector: Injector): Unit = {
    this.injector = injector
  }

  def props(actorName: String): Props = Props(classOf[GuiceActorProducer], injector, actorName)
}

object GuiceAkkaExtension extends ExtensionId[GuiceAkkaExtensionImpl] with ExtensionIdProvider {

  /** Register ourselves with the ExtensionIdProvider */
  override def lookup(): GuiceAkkaExtension.type = GuiceAkkaExtension

  /** Called by Akka in order to create an instance of the extension. */
  override def createExtension(system: ExtendedActorSystem): GuiceAkkaExtensionImpl = new GuiceAkkaExtensionImpl

  /** Java API: Retrieve the extension for the given system. */
  override def get(system: ActorSystem): GuiceAkkaExtensionImpl = super.get(system)

}

/**
 * Mix in with Guice Modules that contain providers for top-level actor refs.
 */
trait GuiceAkkaActorRefProvider {

  def propsFor(system: ActorSystem, name: String): Props = GuiceAkkaExtension(system).props(name)

  def provideActorRef(system: ActorSystem, name: String, actorName: Option[String] = None): ActorRef = {
    actorName map {
      value => system.actorOf(propsFor(system, name), value)
    } getOrElse system.actorOf(propsFor(system, name))
  }

  def provideActorRef(system: ActorSystem, name: String, dispatcher: String, actorName: Option[String]): ActorRef = {
    actorName map {
      value => system.actorOf(propsFor(system, name).withDispatcher(dispatcher), value)
    } getOrElse system.actorOf(propsFor(system, name).withDispatcher(dispatcher))
  }
}
