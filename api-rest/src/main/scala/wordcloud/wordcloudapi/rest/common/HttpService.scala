package wordcloud.wordcloudapi.rest.common

import akka.http.scaladsl.server.Route

trait HttpService {

  def routes: Route
}
