package routes

import Cdata.{DataBase, DroneData}
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives.{complete, path, post, _}
import akka.http.scaladsl.server.Route
import play.api.libs.json.{Json, Reads}

import scala.util.{Failure, Success, Try}

object ApiRouter {
  val route: Route = path("drone") {
    concat(
      post {
        entity(as[String]) { body =>
          implicit val droneRd: Reads[DroneData] = Json.reads[DroneData]
          val drone = Try(droneRd.reads(Json.parse(body)))
          if (!drone.isSuccess || !drone.get.isSuccess) {
            complete((BadRequest, s"Malformed json: $body"))
          } else {
            onComplete(DataBase.newDb.pushData(drone.get.get)) {
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