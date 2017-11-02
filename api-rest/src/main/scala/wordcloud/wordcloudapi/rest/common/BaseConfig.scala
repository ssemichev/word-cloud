package wordcloud.wordcloudapi.rest.common

import java.net.InetAddress

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import wordcloud.common.config.ConfigFactoryExt

import scala.util.Try

trait BaseConfig {

  def config: Option[Config] = Some(ConfigFactory.load)

  def appRootSectionName: String

  val localAddress: String = InetAddress.getLocalHost.getHostAddress

  val rootConfig: Config = {
    ConfigFactoryExt.enableEnvOverride()
    config match {
      case Some(c) => c.withFallback(ConfigFactory.load())
      case _       => ConfigFactory.load
    }
  }

  protected val application: Config = rootConfig.getConfig(appRootSectionName)

  val appName: String = application.getString("name")
  val appVersion: String = application.getString("version")

  val httpConfig: HttpConfig = {
    HttpConfig(
      rootConfig.getString("http.interface"),
      rootConfig.getInt("http.port"),
      rootConfig.getString("http.crud-user-name"),
      rootConfig.getString("http.crud-user-password")
    )
  }
  val serviceConfig: ServiceConfig = {
    ServiceConfig(appName, appVersion)
  }

  /** Attempts to acquire from environment, then java system properties. */
  def withFallback[T](env: Try[T]): Option[T] = env match {
    case value if Option(value).isEmpty => None
    case _                              => env.toOption
  }

  protected case class HttpConfig(interface: String, port: Int, crudUserName: String, crudUserPassword: String) {
    require(!crudUserName.isEmpty, "http.crud-user-name")
    require(!crudUserPassword.isEmpty, "http.crud-user-password")
  }

  protected case class ServiceConfig(name: String, version: String)

}
