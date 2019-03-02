import Cdata.{DroneData, InMemDB}
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import routes.{BarRouter, FooRouter}
import slick.jdbc.H2Profile.api._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.io.StdIn
import scala.util.Failure


object Server {


  def main(args: Array[String]): Unit = {


    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    val routes = BarRouter.route ~ FooRouter.route

    val bindingFuture = Http().bindAndHandle(routes, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }

}

object TestSlick extends App {

  val newDb = InMemDB()
  newDb.reset(true)
 // newDb.prepare()

  Await.ready(newDb.pushData(DroneData(4, 1, 1, 1, 1.0, defect = false)), Duration(3, SECONDS)).value.get match {
    case Failure(e) => {
      e.printStackTrace()
      throw e
    }
    case _ => println("ok!!")
  }
}

