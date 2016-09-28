lazy val eHelp = (project in file("."))
    .aggregate(eCore)
  
lazy val eCore = project in file("eCore")