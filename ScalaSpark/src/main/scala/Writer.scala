import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010._

object Writer {

  def main(args: Array[String]): Unit = {


    val Spark = SparkSession
      .builder()
      .appName("dronesRD")
      .master("local[*]")
      .getOrCreate()



        val sc = Spark.sparkContext

        val ssc = new StreamingContext(sc, Seconds(1))

        val sqlContext = new org.apache.spark.sql.SQLContext(sc)

        import sqlContext.implicits._



        val kafkaParams = Map[String, Object](
          "bootstrap.servers" -> "localhost:9092",
          "key.deserializer" -> classOf[StringDeserializer],
          "value.deserializer" -> classOf[StringDeserializer],
          "group.id" -> "use_a_separate_group_id_for_each_stream",
          "auto.offset.reset" -> "latest",
          "enable.auto.commit" -> (false: java.lang.Boolean)
        )


        val topics = Array("drone")
        val stream = KafkaUtils.createDirectStream[String, String](
          ssc,
          PreferConsistent,
          Subscribe[String, String](topics, kafkaParams)
        )


        stream.foreachRDD(rdd => {
          if (!rdd.isEmpty()) {
            rdd.map(r => r.value()).toDF().write.mode(SaveMode.Append).text("./data.txt")
          }
        })

        ssc.start()
        ssc.awaitTermination()

  }
}
