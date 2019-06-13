package kafka


import org.apache.spark.sql.SparkSession

object Reader {
  def main(args: Array[String]): Unit = {
    val Spark = SparkSession
      .builder()
      .appName("dronesRD")
      .master("local[*]")
      .getOrCreate()

    val f = Spark.sparkContext.textFile("./data/logs.txt")


    f.foreach(str => println(str))
  }
}
