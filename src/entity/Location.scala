package entity

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
  override def toString = "Location[" + name + "]"
}