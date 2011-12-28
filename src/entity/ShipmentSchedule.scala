package entity
import scala.actors.Actor
import sim._

abstract class ShipmentSchedule(fleet: Fleet) extends Actor {
  def createShipments(time: Int) : List[Shipment]
  
  def act = loop {
    react {
      case Ping(time) => for {
        s <- createShipments(time)
      } fleet.clock ! AfterDelay(1, s, s.next)
      fleet.clock ! Pong(time, this)
    }
  }

  fleet.schd += this
  start()
}

class SimpleSchedule(fleet: Fleet, src: Location, dst: Location, size: Int = 3)
  extends ShipmentSchedule(fleet) {
  
  def createShipments(time: Int) = 
    if (time % 5 == 0) List(Shipment(fleet, src, dst, size)) else List()
}