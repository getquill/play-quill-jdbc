name := """play-quill-jdbc"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.18"

libraryDependencies ++= Seq(
  jdbc,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.3" % Test,
  "com.h2database" % "h2" % "1.4.190",
  "io.getquill" %% "quill-jdbc" % "1.2.1",
  "com.typesafe.play" %% "play-jdbc-evolutions" % "2.6.25"
)

resolvers ++= Seq(
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
)

routesGenerator := InjectedRoutesGenerator