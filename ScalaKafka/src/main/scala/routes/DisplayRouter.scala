package routes

import Cdata.DataBase
import akka.http.scaladsl.model.StatusCodes.InternalServerError
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{complete, _}
import akka.http.scaladsl.server.Route
import html.DroneList

import scala.util.{Failure, Success}

object DisplayRouter {
  val route: Route = path("") {
    get {
      onComplete(DataBase.newDb.all()) {
        case Success(value) => complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, DroneList.apply(value).toString()))
        case Failure(exception) => complete((InternalServerError, s"An error occurred: ${exception.getMessage}"))
      }
    }
  }
}