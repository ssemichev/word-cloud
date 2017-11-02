package wordcloud.wordcloudapi.rest.datacontracts

import wordcloud.common.traits.Contract

final case class WordCloudSearchRequest(
  top: Option[Int] = None
) extends Contract