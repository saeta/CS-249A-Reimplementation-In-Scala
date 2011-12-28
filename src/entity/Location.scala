package entity
import scala.actors.Actor
import sim._

// DIfficulty?
trait LocType
case object CUST extends LocType
case object PORT extends LocType
case object TTRM extends LocType // Truck Terminal
case object BTRM extends LocType // Boat Terminal
case object PTRM extends LocType // Plane Terminal

case class Location(fleet: Fleet, name: String, lt: LocType = PORT) {
  val segs = scala.collection.mutable.Set[Segment]()
  fleet.locs += this
  def gvName = "location_" + name
  def gvString = gvName + "[label=\"" + name + "\" shape=circle]"
  override def toString = "Location[" + name + "]"
}
