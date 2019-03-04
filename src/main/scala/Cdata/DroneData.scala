package Cdata

import java.sql.Timestamp

import slick.jdbc.H2Profile.api._

case class DroneData(id: Int, latitude: Double, longitude: Double, temperature: Double, battery: Double, defect: Int, time:Int)

class DroneDatas(tag: Tag) extends Table[DroneData](tag, "DRONE_DATA") {
  def line_id = column[Int]("id", O.AutoInc, O.PrimaryKey)
  def id = column[Int]("DRONE_ID")
  def latitude = column[Double]("LATITUDE")
  def longitude = column[Double]("LONGITUDE")
  def temperature = column[Double]("TEMPERATURE")
  def battery = column[Double]("BATTERY")
  def defect = column[Int]("DEFECT")
  def time = column[Int]("TIME")
  def * = (id, latitude, longitude, temperature, battery, defect, time) <> (DroneData.tupled, DroneData.unapply)
}

/*

{"id":1,"latitutde":1.1,"longitude":1,"temperature":9.7,"battery":1,"defect":false}

 */