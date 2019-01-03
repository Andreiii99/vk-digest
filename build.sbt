name := "vk-digest"

version := "1.1.2"

scalaVersion := "2.12.8"

libraryDependencies += "com.vk.api" % "sdk" % "0.5.12"
libraryDependencies += "io.spray" %% "spray-json" % "1.3.5"
libraryDependencies += "com.github.daddykotex" %% "courier" % "1.0.0"
libraryDependencies += "net.codingwell" %% "scala-guice" % "4.2.2"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % Test
libraryDependencies += "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % Test

lazy val root = (project in file(".")).enablePlugins(SbtTwirl)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case PathList("META-INF", xs@_*) => MergeStrategy.filterDistinctLines
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}