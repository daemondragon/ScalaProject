package Cdata

import slick.jdbc.H2Profile.api._
import slick.jdbc.meta.MTable

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.util.Failure
import scala.concurrent.ExecutionContext.Implicits.global

case class InMemDB() extends DataHandler {
  val data = TableQuery[DroneDatas]
  val db = Database.forConfig("inmemory")
  val tables = List(data)

  def prepare(): Future[List[Unit]] = {

    val existing: Future[Vector[MTable]] = db.run(MTable.getTables)
    existing.flatMap(v => {
      val names: Seq[String] = v.map(mt => mt.name.name)
      val createIfNotExist =
        tables
          .filter(table => !names.contains(table.baseTableRow.tableName))
          .map(x => x.schema.create)
      db.run(DBIO.sequence(createIfNotExist))
    })
  }

  def pushData(drone_data: DroneData): Future[Unit] = {
    val addData = DBIO.seq(
      data += drone_data
    )
    db.run(addData)
  }

  def reset(re_prepare: Boolean = false): Future[List[Unit]] = {
    println("Droping tables")
    val existing = db.run(MTable.getTables)


    val setup = existing.flatMap(v => {
      val names = v.map(mt => mt.name.name)
      val dropIfExists =
        tables
          .filter(table => names.contains(table.baseTableRow.tableName))
          .map(_.schema.drop)
      db.run(DBIO.sequence(dropIfExists))
    })


    if (re_prepare)
      setup.flatMap(_ => prepare())
    else
      setup

  }

  override def all(): Future[Seq[DroneData]] = db.run(data.result)
}
