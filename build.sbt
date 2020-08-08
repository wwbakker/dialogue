lazy val root = (project in file(".")).
  settings(
    resolvers += Resolver.url("typesafe", url("https://repo.typesafe.com/typesafe/ivy-releases/"))(Resolver.ivyStylePatterns),

    scalaVersion := "2.13.3",
    scalacOptions ++= Seq("-deprecation"),

    libraryDependencies += "com.typesafe.play" %% "play-json" % "2.9.0",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0" % "test",
    libraryDependencies += "com.lihaoyi" %% "os-lib" % "0.7.1",
  )