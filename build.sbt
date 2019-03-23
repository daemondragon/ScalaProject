name := "ScalaProject"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.10"
libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.1.7"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.21"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.3.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.0"
)


libraryDependencies += "com.h2database" % "h2" % "1.4.196"

val root = (project in file("."))
  .enablePlugins(SbtTwirl)
  .settings(//Set the twirl resource directory
    sourceDirectories in (Compile, TwirlKeys.compileTemplates) +=
      (baseDirectory.value.getParentFile / "src" / "main" / "twirl")
  )



