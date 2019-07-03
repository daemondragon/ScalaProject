import slick.jdbc.H2Profile.api._

case class DroneData(id: Int, latitude: Double, longitude: Double, temperature: Double, battery: Double, defect: Int, time:Int)
/*
{"id":1,"latitude":1.1,"longitude":4,"temperature":9.7,"battery":5,"defect":1,"time":5984984}
 */