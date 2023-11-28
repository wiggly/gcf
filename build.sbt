ThisBuild / assemblyMergeStrategy := {
  case "application.conf" => MergeStrategy.preferProject
  case "remove.txt" => MergeStrategy.discard
  case x =>
    val oldStrategy = (ThisBuild / assemblyMergeStrategy).value
    oldStrategy(x)
}

val root = (project in file("."))
  .settings(
      name := "GCF",
      moduleName := "gcf",
      scalaVersion := "2.13.7",
      run / fork := true,
      Test / parallelExecution := false,
      assembly / mainClass := Some("wiggly.CloudFunction"),
      assembly / assemblyJarName := "wiggly.jar",
  )
  .withDependencies
