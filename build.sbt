name := "vk-digest"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies += "com.vk.api" % "sdk" % "0.5.6"
libraryDependencies += "io.spray" %% "spray-json" % "1.3.3"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % Test
libraryDependencies += "org.scalamock" %% "scalamock-scalatest-support" % "3.5.0" % Test