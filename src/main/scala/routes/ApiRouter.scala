package routes

import java.util.Properties

import Cdata.{DataBase, DroneData}
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives.{complete, path, post, _}
import akka.http.scaladsl.server.Route
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import play.api.libs.json.{Json, Reads, Writes}

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

object ApiRouter {

  val topic = "drone"

  val props = new Properties()
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
  props.put(ProducerConfig.CLIENT_ID_CONFIG, "DroneProducer")
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")

  val producer = new KafkaProducer[String, String](props)

  val route: Route = path("drone") {
    concat(
      post {
        entity(as[String]) { body =>
          implicit val droneRd: Reads[DroneData] = Json.reads[DroneData]
          val drone = Try(droneRd.reads(Json.parse(body)))
          if (!drone.isSuccess || !drone.get.isSuccess) {
            complete((BadRequest, s"Malformed json: $body"))
          } else {
            val newDrone = drone.get.get
            val future = /*DataBase.newDb.pushData(newDrone) */ {
              implicit val droneWt: Writes[DroneData] = Json.writes[DroneData]
              val record = new ProducerRecord[String, String](
                topic,
                newDrone.id.toString,
                droneWt.writes(newDrone).toString()
              )


              // There is no way to convert java to scala future
              // so here is the dirty way to make it work.
              Future.successful(producer.send(record).get())
            }

            onComplete(future) {
              case Success(res) => complete(s"Sent body: $body")
              case Failure(ex) => complete((InternalServerError, s"An error occurred: ${ex.getMessage}"))
            }
          }
        }
      },
      delete {
        onComplete(DataBase.newDb.deleteAllData()) {
          case Success(_) => complete("Done")
          case Failure(ex) => complete((InternalServerError, s"An error occurred: ${ex.getMessage}"))
        }
      }
    )
  }
}