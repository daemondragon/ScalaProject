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
    var hot = 0
    var called = 0

    val north = value
      .map(new Gson().fromJson(_, classOf[DroneData]))
      .filter(_.latitude >= 0)
      .count()
    val south = value
      .map(new Gson().fromJson(_, classOf[DroneData]))
      .filter(_.latitude < 0)
      .count()


    val low = value
      .map(new Gson().fromJson(_, classOf[DroneData]))
      .filter(_.battery <= 15)
      .count()
    val notlow = value
      .map(new Gson().fromJson(_, classOf[DroneData]))
      .filter(_.battery > 15)
      .count()

    /*  var test = 0
    for (str <- value) {
      val g = new Gson()

      val drone = g.fromJson(str, classOf[DroneData])
      if (drone.defect == 1) {
        if (drone.latitude >= 0) {
          north = north + 1
        } else {
          south = south + 1
        }
        if (drone.temperature >= 20.0) {
          hot = hot + 1
        } else {
          called = called + 1
        }
        if (drone.battery < 15.0) {
          low = low + 1
        } else {
          notlow = notlow + 1
        }
      }
      test = test + 1
      println(test)
    }
    println(test)
*/
    println("Proportion of failing devices in north hemisphere : " + (100 * (north / (north+south))).toString() + "%")

    if (hot > called) {
      println("There's more failing devices when temperature is over 20°C")
    } else {
      println("There's more failing devices when temperature is bellow 20°C")
    }
    println(((low / (low + notlow))*100).toString() + "% of devices fails because of low battery or empty fuel tank")

  }

  /*
  In proportion to the whole number of devices is there more failing devices in the north hemisphere or the south hemisphere?
  Is there more failing devices when the weather is hot or when the weather is called?
  Among the failing devices which percentage fails beceause of low battery or empty fuel tank?
   */
}
