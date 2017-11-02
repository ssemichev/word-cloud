import Common._
import Settings._
import Tasks._
import sbt.Keys._

name := "wordcloud"

lazy val publishedProjects = Seq[ProjectReference](
  TestKitProject,
  CommonProject,
  WordCloudServicesProject,
  WordCloudApiServiceProject,
  WordCloudApiRestProject
)

lazy val root = BuildProject("wordcloud-root", ".")
  .settings(notPublish: _*)
  .settings(Testing.settings: _*)
  .aggregate(publishedProjects: _*)

// ---------------------------------------------------------------------------------------------------------------------
// Modules
// ---------------------------------------------------------------------------------------------------------------------

lazy val TestKitProject = BuildProject("testkit")
  .settings(
    libraryDependencies ++= Dependencies.testkit
  )
  .settings(notPublish: _*)
  .dependsOn(CommonProject)

lazy val CommonProject = BuildProject("common")
  .settings(
    libraryDependencies ++= Dependencies.common
  )

lazy val WordCloudServicesProject = BuildProject("wordcloud-services", "services")
  .settings(
    libraryDependencies ++= Dependencies.wordCloudServices
  )
  .dependsOn(CommonProject, TestKitProject % "test,it,e2e,bench")

lazy val WordCloudApiServiceProject = BuildProject("wordcloud-api-service", "api-service")
  .settings(
    libraryDependencies ++= Dependencies.wordCloudApiService
  )
  .dependsOn(CommonProject, WordCloudServicesProject, TestKitProject % "test,it,e2e,bench")

lazy val WordCloudApiRestProject: Project = BuildProject("wordcloud-api-rest", "api-rest")
  .enablePlugins(JavaAppPackaging, DockerPlugin, AshScriptPlugin, BuildInfoPlugin, GitVersioning)
  .settings(
    libraryDependencies ++= Dependencies.wordCloudApiRest,
    dockerExposedPorts := Seq(9000)
  )
  .settings(dockerSettings: _*)
  .settings(releaseSettings: _*)
  .settings(Settings.buildInfoSettings: _*)
  .dependsOn(CommonProject, WordCloudApiServiceProject, TestKitProject % "test,it,e2e,bench")

gitHeadCommitSha in ThisBuild := gitHeadCommitShaDef
gitHeadCommitShaShort in ThisBuild := gitHeadCommitShaShortDef

addCommandAlias("all", "alias")
