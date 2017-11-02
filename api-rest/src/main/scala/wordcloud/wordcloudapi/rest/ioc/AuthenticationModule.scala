package wordcloud.wordcloudapi.rest.ioc

import javax.inject.{ Inject, Named }

import com.google.inject.{ PrivateModule, Provides, Singleton }
import wordcloud.wordcloudapi.rest.common.AppConfig
import wordcloud.wordcloudapi.rest.common.authentication.{ ConfigUserPassAuthenticator, UserPassAuthenticator }
import net.codingwell.scalaguice.ScalaPrivateModule

private[wordcloud] class AuthenticationModule extends PrivateModule with ScalaPrivateModule {

  override def configure(): Unit = {
    expose[UserPassAuthenticator[String]].annotatedWithName("commandEndpoint")
  }

  @Provides
  @Singleton
  @Named("commandEndpoint")
  def provideUserPassAuthenticatorForSearchEndpoint(@Inject() config: AppConfig): UserPassAuthenticator[String] =
    ConfigUserPassAuthenticator(config.httpConfig.crudUserName, config.httpConfig.crudUserPassword)
}