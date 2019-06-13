package Cdata

import slick.jdbc.H2Profile.api._
import scala.concurrent.{Future}
import scala.concurrent.ExecutionContext.Implicits.global

case class InMemDB() extends DataHandler {
  val data = TableQuery[DroneDatas]
  val db = Database.forConfig("inmemory")
  val tables = List(data)

  def prepare(): Future[List[Unit]] = {
    Future.sequence(tables.map(tab => db.run(tab.schema.createIfNotExists)))
  }

  def pushData(drone_data: DroneData): Future[Unit] = {
    val addData = DBIO.seq(
      data += drone_data
    )
    db.run(addData)
  }

  def drop(): Future[List[Unit]] = {
    Future.sequence(tables.map(tab => db.run(tab.schema.dropIfExists)))
  }

  def deleteAllData(): Future[List[Int]] = {
    Future.sequence(tables.map(tab => db.run(tab.delete)))
  }

  override def all(): Future[Seq[DroneData]] = db.run(data.result)
}
