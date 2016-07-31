name := "eCore"

version := "1.0"

organization := "EqualExperts - Lisbon"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.specs2"              %% "specs2-core"         % "[2.4.7,2.4.17]"   % Test,
  "com.github.rhyskeepence" %% "clairvoyance-specs2" % "1.0.119"          % Test
)
    