package wordcloud.wordcloudapi.rest.common

import wordcloud.common.traits.Contract

class AppConfig extends BaseConfig with Contract {

  override def appRootSectionName: String = "wordcloud-service"
}
