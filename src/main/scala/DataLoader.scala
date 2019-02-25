import java.io.File

import com.github.tototoshi.csv.CSVReader
import play.api.libs.json.{JsValue, Json}

import scala.io.Source

object DataLoader {
  def listFiles(file: File): List[File] =
    if (file.isDirectory) //List all files recursively
      file :: file.listFiles().flatMap(listFiles).toList
    else
      List(file)

  def loadJsonData(file: File): List[JsValue] = Source.fromFile(file)
    .getLines() // Each file is not strictly an array of Json, it contains a Json Value per lines.
    .map(Json.parse)
    .toList

  def loadCsvData(file: File): List[Map[String, String]] = CSVReader.open(file)
    .allWithHeaders()

  def loadData(files: List[File]): List[Either[JsValue, Map[String, String]]] = files
    .flatMap(file =>
      if (file.getName.endsWith(".json")) loadJsonData(file).map(Left(_))
      else if (file.getName.endsWith(".csv")) loadCsvData(file).map(Right(_))
      else Nil
    )
}
