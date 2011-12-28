package entity

import adt._
import scala.actors.Actor
import scala.actors.Actor.self
import sim._

// LOWPRI: Difficulty? Return Segment?
case class Segment(fleet: Fleet, src: Location, dst: Location, mode: Mode, 
    len: Length, difficulty: Difficulty = Difficulty(1)) extends Actor {
  src.segs += this
  fleet.segs += this
  start()
  
  private var nextAvailableTime = 0 
  def time = len / fleet.speed(mode)
  def cost = len * difficulty * fleet.cost(mode)
  override def toString = src.name + "->" + dst.name
  def gvString = src.gvName + " -> " + dst.gvName
  
  def act() {
    loop {
      react {
        case Ping(time) =>
          // We can send at soonest the next time slot.
          nextAvailableTime = math.max(time + 2, nextAvailableTime)
          fleet.clock ! Pong(time, self)
        case msg => handleSimMessage(msg)
      }
    }
  }
  
  def handleSimMessage(msg: Any) = msg match {
    case shp: Shipment =>
      shp.moved()
      if (dst == shp.dst) shp.completed()
      else {
        nextAvailableTime += shp.size // TODO: take into account capacity
        fleet.clock !  WorkItem(nextAvailableTime, shp, shp.next)
      }
  }
}
