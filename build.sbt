name := "transport-router"

version := "0.1"

scalaVersion := "2.13.1"

mainClass in (Compile, run) := Some("com.transportrouter.Main")
mainClass in (Compile, packageBin) := Some("com.transportrouter.Main")

libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.0" % "test"
