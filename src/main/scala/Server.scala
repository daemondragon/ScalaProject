import Cdata.DataBase
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import routes.{ApiRouter, DisplayRouter}

import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.io.StdIn
import scala.concurrent.duration._

object Server {
  def main(args: Array[String]): Unit = {

    // Resets the data at each start (might want to  change that after tests)
    //DataBase.newDb.reset(true)

//    Await.result(DataBase.newDb.drop(), Duration.Inf)

    val future = DataBase.newDb.prepare().map(_ => {
      implicit val system = ActorSystem("my-system")
      implicit val materializer = ActorMaterializer()
      // needed for the future flatMap/onComplete in the end
      implicit val executionContext = system.dispatcher
      val routes = ApiRouter.route ~ DisplayRouter.route
      val bindingFuture = Http().bindAndHandle(routes, "localhost", 8080)

      println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
      StdIn.readLine() // let it run until user presses return
      bindingFuture
        .flatMap(_.unbind()) // trigger unbinding from the port
        .onComplete(_ => system.terminate()) // and shutdown when done
    })

    Await.result(future, Duration.Inf)

  }

}
