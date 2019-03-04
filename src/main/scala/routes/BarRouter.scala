package routes

import Cdata.{DataBase, DroneData}
import akka.http.scaladsl.server.Directives.{complete, path, post, _}
import akka.http.scaladsl.server.Route
import play.api.libs.json.{Json, OWrites, Reads}

import scala.util.{Failure, Success}
import akka.http.scaladsl.model.StatusCodes._

object BarRouter {
  val route: Route = path("bar") {
    post {
      entity(as[String]) { body =>

        implicit val droneRd: Reads[DroneData] = Json.reads[DroneData]
        implicit val droneWr: OWrites[DroneData] = Json.writes[DroneData]


        val drone = droneRd.reads(Json.parse(body))

        val defdrone = DroneData(1, 1.1, 1, 9.7, 1, false)
        val default = Json.toJson(defdrone)
        if (!drone.isSuccess) {

          complete((BadRequest, s"$default"))
        } else {
          onComplete(DataBase.newDb.pushData(drone.get)) {
            case Success(res) => complete(s"Sent body: $body")
            case Failure(ex) => complete((InternalServerError, s"An error occurred: ${ex.getMessage}"))
          }
        }


      }
    }
  }
}