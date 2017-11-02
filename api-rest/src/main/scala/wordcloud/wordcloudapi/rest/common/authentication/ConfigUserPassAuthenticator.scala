package wordcloud.wordcloudapi.rest.common.authentication

import akka.http.scaladsl.server.directives.Credentials.Provided
import akka.http.scaladsl.server.directives.{ Credentials, SecurityDirectives }

case class ConfigUserPassAuthenticator(userName: String, userPassword: String) extends UserPassAuthenticator[String] {

  override def authenticate: SecurityDirectives.Authenticator[String] = {
    case provided @ Credentials.Provided(name) if isValid(provided, name) => Some(name)
    case _ =>
      // If wrong credentials were given, then this route is not completed before 1 second has passed.
      // This makes timing attacks harder.
      val delay = 1000L
      Thread.sleep(delay)
      None
  }

  private[this] def isValid(provided: Provided, id: String): Boolean = {
    provided.verify(userPassword) && id == userName
  }
}