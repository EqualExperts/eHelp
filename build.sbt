lazy val root = (project in file(".")).aggregate(eCore)
  
lazy val eCore = project in file("eCore")