package wordcloud.wordcloudapi.rest

import akka.event.NoLogging
import wordcloud.testkit.UnitTestSpec

class PackageSpec extends UnitTestSpec {

  behavior of "WordCloudApi package"

  it should "count an action elapsed time" in {
    time(
      "test", "testAction"
    )(NoLogging) shouldBe "test"
  }

}
