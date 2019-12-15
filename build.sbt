name := "nearby"

version := "0.1"

scalaVersion := "2.13.1"

mainClass in (Compile, run) := Some("com.nearby.Main")
mainClass in (Compile, packageBin) := Some("com.nearby.Main")

libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.0" % "test"
