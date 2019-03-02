package routes

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

object FooRouter {
  val route: Route = path("foo") {
    get {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "This is foo!"))
    }
  }
}