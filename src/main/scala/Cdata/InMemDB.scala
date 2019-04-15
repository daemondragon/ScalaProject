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

  def prepare(): Unit = {
    println("Trying to create tables")

    val existing = db.run(MTable.getTables)
    val setup = existing.flatMap(v => {
      val names = v.map(mt => mt.name.name)
      val createIfNotExist = tables.filter(table =>
        !names.contains(table.baseTableRow.tableName)).map(_.schema.create)
      db.run(DBIO.sequence(createIfNotExist))
    })


    // We dont want to give an object without the DB properly setup
    Await.ready(setup, Duration(3, SECONDS)).value.get match {
      case Failure(e) => {
        e.printStackTrace()
        throw e
      }
      case _ =>
    }
    println("Tables creation complete")
  }

  def pushData(drone_data: DroneData): Future[Unit] = {
    val addData = DBIO.seq(
      data += drone_data
    )
    db.run(addData)
  }

  def reset(re_prepare: Boolean = false): Unit = {
    println("Droping tables")
    val existing = db.run(MTable.getTables)
    val setup = existing.flatMap(v => {
      val names = v.map(mt => mt.name.name)
      val dropIfExists = tables.filter(table =>
        names.contains(table.baseTableRow.tableName)).map(_.schema.drop)
      db.run(DBIO.sequence(dropIfExists))
    })

    // Same as before, the await is only here so the program doesn't use an 'incorrect'(ie: not properly setup) db
    Await.ready(setup, Duration(3, SECONDS)).value.get match {
      case Failure(e) => {
        e.printStackTrace()
        throw e
      }
      case _ =>
    }
    println("Drop complete")
    if (re_prepare)
      prepare()
  }

  override def all(): Future[Seq[DroneData]] = db.run(data.result)
}
