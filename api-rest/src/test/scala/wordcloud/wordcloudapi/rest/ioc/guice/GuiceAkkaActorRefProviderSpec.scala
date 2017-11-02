package wordcloud.wordcloudapi.rest.ioc.guice

import akka.actor.{ Actor, ActorRef, ActorSystem }
import akka.pattern.ask
import akka.testkit.{ ImplicitSender, TestKit }
import com.google.inject._
import com.google.inject.name.Names
import com.typesafe.config.ConfigFactory
import wordcloud.common._
import wordcloud.common.config.ConfigModule
import wordcloud.wordcloudapi.rest.ioc.guice.TestActor.GetDispatcherName
import net.codingwell.scalaguice.ScalaModule
import org.scalatest._

import scala.collection.JavaConverters._
import scala.concurrent.{ Await, Future }
import scala.concurrent.duration._
import scala.language.postfixOps

class GuiceAkkaActorRefProviderSpec extends TestKitWithSystemSpec(ActorSystem("test-system", ConfigFactory.parseString(
  """
    |test-dispatcher {
    |  throughput = 1
    |}
  """.stripMargin
))) {

  trait AkkaGuiceInjector {

    def initInjector(testModules: Module*): Unit = {
      val modules = List(
        new ConfigModule(),
        new AbstractModule with ScalaModule {
          override def configure(): Unit = {
            bind[Actor].annotatedWith(Names.named(TestActor.name)).to[TestActor]
          }

          @Provides
          def provideSystem(): ActorSystem = system
        }
      ) ++ testModules

      GuiceAkkaExtension(system).initialize(Guice.createInjector(modules.asJava))
    }
  }

  trait GuiceManagedActor extends AkkaGuiceInjector with GuiceAkkaActorRefProvider {
    initInjector()
  }

  "a Guice-managed actor" should {

    "be instantiated by name" in new GuiceManagedActor {
      lazy val actor: ActorRef = provideActorRef(system, TestActor.name)
      actor should be isDefined

      actor.path.name should not be empty
    }

    "be instantiated by name with custom name" in new GuiceManagedActor {
      lazy val actor: ActorRef = provideActorRef(system, TestActor.name, actorName = Some(TestActor.name))
      actor should be isDefined

      actor.path.name should not be empty
      actor.path.name shouldBe TestActor.name
    }

    "be instantiated with the specified dispatcher set" in new GuiceManagedActor {
      lazy val actor: ActorRef = provideActorRef(system, TestActor.name, dispatcher = "test-dispatcher", actorName = None)
      actor should be isDefined

      actor.path.name should not be empty

      val duration: FiniteDuration = 3.seconds
      val result: Future[String] = actor.ask(GetDispatcherName)(duration).mapTo[String]
      Await.result(result, duration) shouldBe "Dispatcher[test-dispatcher]"

    }

    "be instantiated with the specified dispatcher set and custom name" in new GuiceManagedActor {
      val actorName: String = s"${TestActor.name}-with-dispatcher"
      lazy val actor: ActorRef = provideActorRef(system, TestActor.name, dispatcher = "test-dispatcher", actorName = Some(actorName))
      actor should be isDefined

      actor.path.name should not be empty
      actor.path.name shouldBe actorName
    }
  }
}

abstract class TestKitSpec(name: String)
    extends TestKit(ActorSystem(name))
    with WordSpecLike
    with Matchers
    with BeforeAndAfterAll
    with ImplicitSender {

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }
}

abstract class TestKitWithSystemSpec(actorSystem: ActorSystem)
    extends TestKit(actorSystem)
    with WordSpecLike
    with Matchers
    with BeforeAndAfterAll
    with ImplicitSender {

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }
}