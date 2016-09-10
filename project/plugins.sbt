// It recompiles and runs the app every time the code in files changes. Notice that it is initialized inside build.sbt
addSbtPlugin("io.spray" % "sbt-revolver" % "0.8.0")

// Lets us deploy the app as a single .jar file
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.3")

// Needed to deploy the app into Heroku
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.1.0-RC1")