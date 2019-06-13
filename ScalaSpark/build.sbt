name := "spark_streaming_test"

version := "0.1"

scalaVersion := "2.12.8"


libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.3.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.0"
)


libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.3"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "2.4.3"
libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka-0-10" % "2.4.3"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.3"


