package wordcloud.wordcloudapi.rest.ioc

import com.google.inject.AbstractModule
import wordcloud.common.config.ConfigModule
import wordcloud.wordcloudapi.rest.common.AppConfig
import net.codingwell.scalaguice.ScalaModule

private[wordcloud] class Bootstrapper extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    bindConfiguration()

    install(new ConfigModule)
    install(new AkkaModule)
    install(new AuthenticationModule)
    install(new ServicesModule)
    install(new EndpointsModule)
  }

  /**
   * Binds the application configuration to the [[AppConfig]] interface.
   *
   * The config is bound as an eager singleton so that errors in the config are detected
   * as early as possible.
   */
  private[this] def bindConfiguration() = {
    bind[AppConfig].toProvider[AppConfigProvider].asEagerSingleton()
  }
}