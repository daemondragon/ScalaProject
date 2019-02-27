package routes

import akka.http.scaladsl.server.Directives.{complete, get, path, post}
import akka.http.scaladsl.server.Directives._


object BarRouter {
  val route = path("bar") {
    post {
      entity(as[String]) { body =>
        complete(s"Sent body: $body")
      }
    }
  }
}