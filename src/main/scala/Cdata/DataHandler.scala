package Cdata

import scala.concurrent.Future

trait DataHandler {

  def pushData(data: DroneData): Future[Unit]

  def prepare(): Future[List[Unit]]

  def reset(re_prepare: Boolean = false): Future[List[Unit]]

  def all(): Future[Seq[DroneData]]
}
