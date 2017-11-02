package wordcloud.wordcloudapi.rest

import com.google.inject.name.Names
import wordcloud.wordcloudapi.rest.common.Endpoint
import wordcloud.wordcloudapi.rest.ioc.Global

object JobQueryEndpointApp extends App {

  Global.getInstance[Endpoint](Names.named("Jobs")).publish()
}