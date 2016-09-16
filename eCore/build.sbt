enablePlugins(JavaAppPackaging)

name := """eCore"""
version := "1.0"
organization := "EqualExperts - Lisbon"
scalaVersion := "2.11.8"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaV         = "2.4.8"
  val specs2V       = "[2.4.7,2.4.17]"
  val clairvoyanceV = "1.0.123"
  Seq(
    "com.typesafe.akka"       %% "akka-actor"                        % akkaV,
    "com.typesafe.akka"       %% "akka-stream"                       % akkaV,
    "com.typesafe.akka"       %% "akka-http-experimental"            % akkaV,
    "com.typesafe.akka"       %% "akka-http-spray-json-experimental" % akkaV,
    "com.typesafe.akka"       %% "akka-http-testkit"                 % akkaV,
    "org.specs2"              %% "specs2-core"                       % specs2V        % Test,
    "com.github.rhyskeepence" %% "clairvoyance-specs2"               % clairvoyanceV  % Test
  )
}

Revolver.settings

fork in run := true
    