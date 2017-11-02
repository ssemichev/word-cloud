package wordcloud.wordcloudapi.rest.ioc

import java.lang.annotation.Annotation

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Stage
import wordcloud.common.traits.ApplicationGlobalBase
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector

object Global extends ApplicationGlobalBase {

  lazy val injector: Injector = Guice.createInjector(Stage.PRODUCTION, new Bootstrapper)

  override def getInstance[T: Manifest]: T = injector.instance[T]

  override def getInstance[T: Manifest](ann: Annotation): T = injector.instance[T](ann)
}
