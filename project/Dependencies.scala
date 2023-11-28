import sbt.Keys.libraryDependencies
import sbt._

object Dependencies extends AutoPlugin {

  object autoImport {
    implicit class DependencySettings(val project: Project) extends AnyVal {
      def withDependencies : Project = project
        .settings(libraryDependencies ++= deps.value)
        .settings(libraryDependencies ++= testDeps.value)
    }
  }

  // Versions
  val scalaTestVersion = "3.2.9"
  val http4sCloudFunctionsVersion = "0.4.3"

  lazy val deps = Def.setting(Seq(
    "de.killaitis" %% "http4s-cloud-functions" % http4sCloudFunctionsVersion
  ))

  lazy val testDeps = Def.setting(Seq(
    "org.scalatest" %% "scalatest" % scalaTestVersion
  ).map(_ % Test))
}
