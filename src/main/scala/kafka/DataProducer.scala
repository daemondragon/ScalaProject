package kafka

import java.io.File
import java.util.Properties

import client.DataLoader
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

object DataProducer {
  def main(args: Array[String]): Unit = {
    val topic = "drone"

    val props = new Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(ProducerConfig.CLIENT_ID_CONFIG, "DroneProducer")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")

    val producer = new KafkaProducer[String, String](props)

    DataLoader.loadData(DataLoader.listFiles(new File("./data")))
      // Transform the jsObject to a producer record.
      .map(jsObject => new ProducerRecord[String, String](topic, jsObject.value("id").toString(), jsObject.toString()))
      // Send all object to kafka
      .map(record => producer.send(record))
      // Wait for the response
      .map(future => future.get())
      // And display it (not needed, but for test purpose)
      .foreach(response => println(s"topic: ${response.topic()}, partition: ${response.partition()}, offset: ${response.offset()}"))

  }
}
