import java.io.File

import com.github.tototoshi.csv.CSVReader
import play.api.libs.json.{JsObject, JsString, JsValue, Json}

import scala.io.Source

object DataLoader {
  def listFiles(file: File): List[File] =
    if (file.isDirectory) //List all files recursively
      file :: file.listFiles().flatMap(listFiles).toList
    else
      List(file)

  def loadJsonData(file: File): List[JsObject] = Source.fromFile(file)
    .getLines() // Each file is not strictly an array of Json, it contains a Json Value per lines.
    .map(Json.parse)
    .map(_.as[JsObject])
    .toList

  def loadCsvData(file: File): List[JsObject] = CSVReader.open(file)
    .allWithHeaders()
    .map(x => Json.toJsObject(x))

  def loadData(files: List[File]): List[JsObject] = files
    .flatMap(file =>
      if (file.getName.endsWith(".json")) loadJsonData(file)
      else if (file.getName.endsWith(".csv")) loadCsvData(file)
      else Nil
    )
}
