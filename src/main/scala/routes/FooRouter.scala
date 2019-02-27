package routes

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._

object FooRouter {
  val route = path("foo") {
    get {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "This is foo!"))
    }
  }
}