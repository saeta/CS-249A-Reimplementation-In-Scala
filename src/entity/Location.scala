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

case class Location(fleet: Fleet, name: String, lt: LocType = PORT) 
    extends Actor {
  var other: Location = _ // TODO: make less suck
  val segs = scala.collection.mutable.Set[Segment]()
  fleet.locs += this
  override def toString = "Location[" + name + "]"
  
  def sendMessages() {
    if (!(other eq null)) {
      val newShip = Shipment(fleet, this, other, 3)
      println("Sending new messages...")
      fleet.clock ! AfterDelay(1, newShip, newShip.next)
    }
  }
  def act() {
    import scala.actors.Actor.self
    loop {
      react {
        case Stop => exit()
        case Ping(time) =>
          if (time % 5 == 0) sendMessages()
          fleet.clock ! Pong(time, self)
        case msg => throw new RuntimeException("Help!" + msg)
      }
    }
  }
  start()
}