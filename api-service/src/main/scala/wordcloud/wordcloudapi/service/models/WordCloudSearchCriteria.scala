package wordcloud.wordcloudapi.service.models

import wordcloud.common.traits.DomainObject

final case class WordCloudSearchCriteria(
  top: Option[Int] = None
) extends DomainObject