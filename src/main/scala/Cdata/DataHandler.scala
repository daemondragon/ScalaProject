package Cdata

import scala.concurrent.Future

trait DataHandler {

  def pushData(data: DroneData): Future[Unit]

  def prepare(): Unit

  def reset(re_prepare: Boolean = false): Unit


}
