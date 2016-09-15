lazy val root = (project in file("."))
    .enablePlugins(JavaAppPackaging)
    .aggregate(eCore)
  
lazy val eCore = project in file("eCore")