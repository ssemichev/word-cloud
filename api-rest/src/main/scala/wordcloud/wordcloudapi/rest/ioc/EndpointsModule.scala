package wordcloud.wordcloudapi.rest.ioc

import com.google.inject.PrivateModule
import com.google.inject.name.Names
import wordcloud.wordcloudapi.rest.JobQueryEndpoint
import wordcloud.wordcloudapi.rest.common.Endpoint
import net.codingwell.scalaguice.ScalaPrivateModule

private[wordcloud] class EndpointsModule extends PrivateModule with ScalaPrivateModule {

  override def configure(): Unit = {
    setupEndpoints()

    expose[Endpoint].annotatedWith(Names.named("Jobs"))
  }

  private[this] def setupEndpoints(): Unit = {

    bind[Endpoint].annotatedWithName("Jobs").to[JobQueryEndpoint]
  }
}