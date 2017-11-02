package wordcloud.wordcloudapi.rest.datacontracts

import wordcloud.common.traits.Contract

final case class WordCloudSearchResponse(
  words: Map[String, Int]
) extends Contract