package entity
import scala.actors.Actor

// DIfficulty?
trait LocType
case object CUST extends LocType
case object PORT extends LocType
case object TTRM extends LocType // Truck Terminal
case object BTRM extends LocType // Boat Terminal
case object PTRM extends LocType // Plane Terminal

case class Location(fleet: Fleet, name: String, lt: LocType = PORT) 
    extends Actor {
  val segs = scala.collection.mutable.Set[Segment]()
  fleet.locs += this
  override def toString = "Location[" + name + "]"
  
  def act() {
    // TODO: if the LocType is cust, spew out Shipments at predefined intervals
    throw new RuntimeException("Unimplemented!")
  }
}