//package kafka
//
//import java.time.Duration
//import java.util.{Collections, Properties}
//
//import Cdata.DroneData
//import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
//import play.api.libs.json.{Json, Reads}
//
//import scala.util.Try
//
//object HDFSConsumer {
//  def main(args: Array[String]): Unit = {
//    val topic = "drone"
//
//    val props = new Properties()
//    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
//    props.put(ConsumerConfig.GROUP_ID_CONFIG, "HDFSConsumer")
//    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
//    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
//    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
//    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100")
//    props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000")
//
//    val consumer = new KafkaConsumer[String, String](props)
//
//    // Subscribing to the wanted topic
//    consumer.subscribe(Collections.singletonList(topic))
//
//    // The reader that will read all drone data from the kafka stream
//    implicit val droneRd: Reads[DroneData] = Json.reads[DroneData]
//
//    while (true) {
//      val records = consumer.poll(Duration.ofSeconds(1))
//
//      println(s"Got ${records.count()} records")
//
//      records
//        // Get all records of the wanted topic
//        .records(topic)
//        .forEach(record => {
//          // Extract all the important information
//          val drone = Try(droneRd.reads(Json.parse(record.value()))).get.get
//          println(drone)
//        })
//    }
//  }
//}
//>>>>>>> f7e5b489b715dda16ab615aad8ff8ad9ae2cc485:src/main/scala/kafka/HDFSConsumer.scala