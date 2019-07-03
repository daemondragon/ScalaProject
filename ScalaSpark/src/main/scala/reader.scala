import com.google.gson.Gson
import org.apache.spark.sql.SparkSession

object reader {
  def main(args: Array[String]): Unit = {
    val Spark = SparkSession
      .builder()
      .appName("dronesRD")
      .master("local[*]")
      .getOrCreate()

    val f = Spark.sparkContext.textFile("./data.txt")

    f.foreach(str => compute(str))
  }

  def compute(str: String) = {
    val g = new Gson()

    val drone = g.fromJson(str, classOf[DroneData])
    if (drone.defect == 1) {
      println(str)
    }
  }

  /*
  In proportion to the whole number of devices is there more failing devices in the north hemisphere or the south hemisphere?
  Is there more failing devices when the weather is hot or when the weather is called?
  Among the failing devices which percentage fails beceause of low battery or empty fuel tank?
   */
}
