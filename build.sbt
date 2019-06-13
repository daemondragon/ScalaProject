name := "ScalaProject"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies += "com.google.code.gson" % "gson" % "2.8.5"
libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.1.7"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.21"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.3.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.0"
)


libraryDependencies += "com.h2database" % "h2" % "1.4.196"
libraryDependencies += "org.apache.kafka" % "kafka_2.12" % "2.2.0"

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.3"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "2.4.3"
libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka-0-10" % "2.4.3"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.3"


val root = (project in file("."))
  .enablePlugins(SbtTwirl)
  .settings(//Set the twirl resource directory
    sourceDirectories in (Compile, TwirlKeys.compileTemplates) +=
      (baseDirectory.value.getParentFile / "src" / "main" / "twirl")
  )



