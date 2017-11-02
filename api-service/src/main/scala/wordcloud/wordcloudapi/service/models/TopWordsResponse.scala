package wordcloud.wordcloudapi.service.models

import wordcloud.common.traits.DomainObject

final case class TopWordsResponse(
  words: Map[String, Int]
) extends DomainObject
