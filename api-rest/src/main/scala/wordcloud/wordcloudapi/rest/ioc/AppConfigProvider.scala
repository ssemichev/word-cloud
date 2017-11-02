package wordcloud.wordcloudapi.rest.ioc

import javax.inject.Inject

import com.google.inject.Provider
import com.typesafe.config.Config
import wordcloud.wordcloudapi.rest.common.AppConfig

class AppConfigProvider @Inject() (configuration: Config) extends Provider[AppConfig] {

  override def get(): AppConfig = {
    new AppConfig {
      override def config: Option[Config] = Some(configuration)
    }
  }
}
