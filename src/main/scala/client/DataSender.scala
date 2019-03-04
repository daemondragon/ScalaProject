package client

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import play.api.libs.json.JsObject

import scala.concurrent.ExecutionContextExecutor

case class DataSender(host: String, port: Int, route: String) {
  def sendData(objects: Iterator[JsObject]): Unit = {
    implicit val system: ActorSystem = ActorSystem()
    val http = Http(system)
    val pool = http.cachedHostConnectionPool[JsObject](host, port)

    implicit val executionContext: ExecutionContextExecutor = system.dispatcher
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    // Sending all objects automatically one by one.

    // From all js objects
    Source.fromIterator(() => objects)
      // Create the associated POST HttpRequest
      .map(data => (HttpRequest(
        method = HttpMethods.POST,
        uri = s"http://$host:$port/$route",
        entity = HttpEntity(ContentTypes.`application/json`, data.toString())
      ), data))
      // Set the used pool
      .via(pool)
      // Launch all request, result is not used as we have nothing to do with them.
      .runForeach(response => println(response._1))

    //Do not wait for termination, all data have been received.
    http.shutdownAllConnectionPools()
  }
}
