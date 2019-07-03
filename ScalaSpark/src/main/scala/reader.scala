import com.google.gson.Gson
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object reader {
  def main(args: Array[String]): Unit = {
    val Spark = SparkSession
      .builder()
      .appName("dronesRD")
      .master("local[*]")
      .getOrCreate()

    val f = Spark.sparkContext.textFile("./data.txt")
    compute(f)
  }
  def compute(value: RDD[String]) = {
    var south = 0
    var north = 0
    var hot = 0
    var called = 0
    var low = 0
    var notlow = 0
    value.foreach(str => {
      val g = new Gson()

      val drone = g.fromJson(str, classOf[DroneData])
      if (drone.defect == 1) {
        if (drone.latitude >= 0) {
          north = north + 1
        } else {
          south = south + 1
        }
        if (drone.temperature >= 20) {
          hot = hot + 1
        } else {
          called = called + 1
        }

        println(str)
      }
    })
    print("Proportion of failing devices in north hemisphere :")
    print(north.toString)
    print("/")
    println((north + south).toString)
    if (hot > called) {
      println("There's more failing devices when temperature is over 20°C")
    } else {
      println("There's more failing devices when temperature is bellow 20°C")
    }

  }

  /*
  In proportion to the whole number of devices is there more failing devices in the north hemisphere or the south hemisphere?
  Is there more failing devices when the weather is hot or when the weather is called?
  Among the failing devices which percentage fails beceause of low battery or empty fuel tank?
   */
}
