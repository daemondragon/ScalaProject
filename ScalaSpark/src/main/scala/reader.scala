import com.google.gson.Gson
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object reader {
  val weatherLimit = 25

  def loadData(): RDD[DroneData] = {
    val Spark = SparkSession
      .builder()
      .appName("dronesRD")
      .master("local[*]")
      .getOrCreate()

    Spark.sparkContext
      .textFile("./data.txt")
      .cache()
      .map(new Gson().fromJson(_, classOf[DroneData]))
  }

  def failedData(): RDD[DroneData] = {
    loadData()
      .filter(data => data.battery <= 0.0 || data.temperature >= 45)
  }

  def failingDeviceLocation(): (Int, Int) = failedData()
    // Say if it is above or under the equator
    .map(data => (data.id, if (data.latitude > 0) (1, 0) else (0, 1)))
    // Take the value for each drone
    .reduceByKey((first, second) => (math.max(first._1, second._1), math.max(first._2, second._2)))
    .map(_._2)
    // Count the number of drone above and under
    .reduce((first, second) => (first._1 + second._1, first._2 + second._2))

  def failingDeviceWeather(): (Int, Int) = failedData()
    // Say if it is above or under the temperature limit
    .map(data => (data.id, if (data.temperature <= weatherLimit) (1, 0) else (0, 1)))
    // Take the value for each drone
    .reduceByKey((first, second) => (math.max(first._1, second._1), math.max(first._2, second._2)))
    .map(_._2)
    // Count the number of drone above and under
    .reduce((first, second) => (first._1 + second._1, first._2 + second._2))

  def failingBatteryPercentage(): (Int, Int) = failedData()
    // Say if it is above or under the equator
    .map(data => (data.id, data.temperature < 45 && data.battery <= 0.0))
    // Take the value for each drone (have failed for battery if only failed for battery)
    .reduceByKey((first, second) => first && second)
    .map(data => (if (data._2) 1 else 0, 1))
    // Count the number of drone above and under
    .reduce((first, second) => (first._1 + second._1, first._2 + second._2))

  def main(args: Array[String]): Unit = {
    //loadData().foreach(println(_))

    val countLocation = failingDeviceLocation()
    val countWeather = failingDeviceWeather()
    val batteryPercentage = failingBatteryPercentage()

    println("Proportion of failing devices in north hemisphere : " + (100.0 * countLocation._1 / math.max(countLocation._1 + countLocation._2, 1)).toString + "%")

    if (countWeather._1 < countWeather._2) {
      println(s"There's more failing devices when temperature is over $weatherLimit°C (${100.0 * countWeather._2 / math.max(countWeather._1 + countWeather._2, 1)}%)")
    } else {
      println(s"There's more failing devices when temperature is bellow $weatherLimit°C (${100.0 * countWeather._1 / math.max(countWeather._1 + countWeather._2, 1)}%)")
    }
    println(s"${batteryPercentage._1 * 100 / math.max(batteryPercentage._2, 1)}% of devices fails because of low battery or empty fuel tank")
  }

  /*
  In proportion to the whole number of devices is there more failing devices in the north hemisphere or the south hemisphere?
  Is there more failing devices when the weather is hot or when the weather is called?
  Among the failing devices which percentage fails beceause of low battery or empty fuel tank?
   */
}
