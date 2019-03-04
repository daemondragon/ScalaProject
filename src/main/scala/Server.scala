import Cdata.DataBase
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import routes.{AddRouter, DisplayRouter}

import scala.io.StdIn


object Server {
  def main(args: Array[String]): Unit = {

    // Resets the data at each start (might wana change that after tests)
    DataBase.newDb.reset(true)


    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher
    val routes = AddRouter.route ~ DisplayRouter.route
    val bindingFuture = Http().bindAndHandle(routes, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done

    

  }

}
