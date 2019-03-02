package Cdata

import slick.jdbc.H2Profile.api._

case class DroneData(drone_id: Int, latitutde: Int, longitude: Int, temperature: Int, battery: Double, defect: Boolean)

class DroneDatas(tag: Tag) extends Table[DroneData](tag, "DRONE_DATA") {
  def id = column[Int]("id", O.AutoInc, O.PrimaryKey)
  def drone_id = column[Int]("DRONE_ID")
  def latitude = column[Int]("LATITUDE")
  def longitude = column[Int]("LONGITUDE")
  def temperature = column[Int]("TEMPERATURE")
  def battery = column[Double]("BATTERY")
  def defect = column[Boolean]("DEFECT")
  def * = (drone_id, latitude, longitude, temperature, battery, defect) <> (DroneData.tupled, DroneData.unapply)
}