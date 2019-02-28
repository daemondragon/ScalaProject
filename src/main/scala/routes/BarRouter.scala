package routes

import akka.http.scaladsl.server.Directives.{complete, path, post, _}


object BarRouter {
  val route = path("bar") {
    post {
      entity(as[String]) { body =>
        complete(s"Sent body: $body")
      }
    }
  }
}