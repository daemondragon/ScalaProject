package routes

import akka.http.scaladsl.server.Directives.{complete, path, post, _}
import akka.http.scaladsl.server.Route


object BarRouter {
  val route: Route = path("bar") {
    post {
      entity(as[String]) { body =>
        complete(s"Sent body: $body")
      }
    }
  }
}