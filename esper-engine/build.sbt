name := "esper-engine"

organization := "es.upm.fi.oeg.morph.streams"

version := "1.0.4"

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.0.13",  
  "com.espertech" % "esper" % "5.0.0" exclude("log4j","log4j"),
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",
  "com.typesafe.akka" %% "akka-actor" % "2.3.4",
  "com.typesafe.akka" %% "akka-remote" % "2.3.4",
  "com.typesafe.akka" %% "akka-kernel" % "2.3.4"
)

resolvers ++= Seq(
  "aldebaran-releases" at "http://aldebaran.dia.fi.upm.es/artifactory/sstreams-releases-local",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"  
)

javacOptions ++= Seq("-source", "1.7", "-target", "1.7")

