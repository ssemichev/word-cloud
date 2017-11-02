import Versions._
import sbt._

object Dependencies {

  // Libraries
  object Compile {
    val jodaTime          = "joda-time"                     %  "joda-time"                          % JodaTimeVersion
    val jodaConvert       = "org.joda"                      %  "joda-convert"                       % JodaConvertVersion
    val json4s            = "org.json4s"                    %% "json4s-jackson"                     % Json4sVersion
    val json4sExt         = "org.json4s"                    %% "json4s-ext"                         % Json4sVersion
    val akkaActor         = "com.typesafe.akka"             %% "akka-actor"                         % AkkaVersion
    val akkaStream        = "com.typesafe.akka"             %% "akka-stream"                        % AkkaVersion
    val akkaHttp          = "com.typesafe.akka"             %% "akka-http"                          % AkkaHttpVersion
    val guice             = "com.google.inject"             %  "guice"                              % GuiceVersion
    val scalaGuice        = "net.codingwell"                %% "scala-guice"                        % ScalaGuiceVersion
    val ficus             = "com.iheart"                    %% "ficus"                              % FicusVersion
    val akkaSlf4j         = "com.typesafe.akka"             %% "akka-slf4j"                         % AkkaVersion
    val logback           = "ch.qos.logback"                %  "logback-classic"                    % LogbackVersion
    val commonsLang       = "org.apache.commons"            %  "commons-lang3"                      % CommonsLangVersion
    val commonsCodec      = "commons-codec"                 %  "commons-codec"                      % CommonsCodecVersion
    val commonsIo         = "commons-io"                    %  "commons-io"                         % CommonsIoVersion
  }

  object Test {
    val akkaTestkit       = "com.typesafe.akka"             %% "akka-testkit"                 % AkkaVersion
    val akkaHttpTestkit   = "com.typesafe.akka"             %% "akka-http-testkit"            % AkkaHttpVersion
    val scalaTest         = "org.scalatest"                 %% "scalatest"                    % ScalaTestVersion
    val scalaMock         = "org.scalamock"                 %% "scalamock-scalatest-support"  % ScalaMockVersion
    val fabricator        = "com.github.azakordonets"       %% "fabricator"                   % FabricatorTestVersion
    val scalaMeterCore    = "com.storm-enroute"             %% "scalameter-core"              % ScalaMeterVersion
    val scalaMeter        = "com.storm-enroute"             %% "scalameter"                   % ScalaMeterVersion
    val groovy            = "org.codehaus.groovy"           %  "groovy-all"                   % GroovyVersion
  }

  val time              = Seq(Compile.jodaConvert, Compile.jodaTime)
  val json4sJson        = Seq(Compile.json4s, Compile.json4sExt)
  val akka              = Seq(Compile.akkaActor, Compile.akkaStream)
  val akkaHttp          = Seq(Compile.akkaHttp) ++ json4sJson
  val di                = Seq(Compile.guice, Compile.scalaGuice)
  val config            = Seq(Compile.ficus)
  val logging           = Seq(Compile.akkaSlf4j, Compile.logback)
  val apacheCommons     = Seq(Compile.commonsLang, Compile.commonsCodec, Compile.commonsIo)

  lazy val shared = logging ++ time ++ apacheCommons ++ di ++ config

  // Sub-project specific dependencies
  lazy val testkit = shared ++
    Seq(Test.akkaTestkit, Test.akkaHttpTestkit, Test.scalaTest, Test.scalaMock,
      Test.groovy, Test.fabricator, Test.scalaMeterCore, Test.scalaMeter)

  lazy val common = shared ++ json4sJson ++ Seq(Test.scalaTest % "test")

  lazy val wordCloudApiService = shared ++ akkaHttp

  lazy val wordCloudApiRest = shared ++ akka
}
