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

    val north = value
      .map(new Gson().fromJson(_, classOf[DroneData]))
      .filter(_.defect == 1)
      .filter(_.latitude >= 0)
      .count()
    val south = value
      .map(new Gson().fromJson(_, classOf[DroneData]))
      .filter(_.defect == 1)
      .filter(_.latitude < 0)
      .count()

    val hot = value
      .map(new Gson().fromJson(_, classOf[DroneData]))
      .filter(_.defect == 1)
      .filter(_.temperature >= 20)
      .count()
    val called = value
      .map(new Gson().fromJson(_, classOf[DroneData]))
      .filter(_.defect == 1)
      .filter(_.temperature < 20)
      .count()

    val low = value
      .map(new Gson().fromJson(_, classOf[DroneData]))
      .filter(_.defect == 1)
      .filter(_.battery <= 15)
      .count()
    val notlow = value
      .map(new Gson().fromJson(_, classOf[DroneData]))
      .filter(_.defect == 1)
      .filter(_.battery > 15)
      .count()

    println("Proportion of failing devices in north hemisphere : " + ((100.0 * north / (north+south))).toString() + "%")

    if (hot > called) {
      println("There's more failing devices when temperature is over 20°C")
    } else {
      println("There's more failing devices when temperature is bellow 20°C")
    }
    println(((100.0 * low / (low + notlow))).toString() + "% of devices fails because of low battery or empty fuel tank")

  }

  /*
  In proportion to the whole number of devices is there more failing devices in the north hemisphere or the south hemisphere?
  Is there more failing devices when the weather is hot or when the weather is called?
  Among the failing devices which percentage fails beceause of low battery or empty fuel tank?
   */
}
