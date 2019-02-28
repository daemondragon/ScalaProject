package client

import java.io.File

import play.api.libs.json.{JsObject, Json}

import scala.io.Source

/**
  * Every function here use iterator to allow lazy-loading:
  * The IO are only performed when needed.
  *
  * Doing so allow to send all loaded data by pieces, preventing
  * the need to load every files into memory before sending them to the server.
  */
object DataLoader {

  def listFiles(file: File): Iterator[File] =
    if (file.isDirectory) //List all files recursively
      Iterator(file) ++ file.listFiles().flatMap(listFiles).toIterator
    else
      Iterator(file)

  def loadJsonData(file: File): Iterator[JsObject] = Source.fromFile(file)
    .getLines() // Each file is not strictly an array of Json, it contains a Json Value per lines.
    .map(Json.parse)
    .map(_.as[JsObject])

  def loadCsvData(file: File): Iterator[JsObject] = {
    val lines = Source.fromFile(file).getLines()
    val headers = lines.next().split(",")//Get the headers to correctly generate the JsObject

    lines
      //Transform the given line to Map[String, Double] (every values is a Number)
      .map(line => (headers zip line.split(",").map(_.toDouble)).toMap)
      //Transform from Map[String, Double] to JsObject
      .map(Json.toJsObject(_))
  }

  def loadData(files: Iterator[File]): Iterator[JsObject] = files
    .flatMap(file =>
      if (file.getName.endsWith(".json")) loadJsonData(file)
      else if (file.getName.endsWith(".csv")) loadCsvData(file)
      else Nil
    )
}
