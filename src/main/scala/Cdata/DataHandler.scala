package Cdata

import scala.concurrent.Future

trait DataHandler {

  def pushData(data: DroneData): Future[Unit]

  def prepare(): Future[List[Unit]]

  def deleteAllData(): Future[List[Int]]

  def drop(): Future[List[Unit]]

  def all(): Future[Seq[DroneData]]
}
