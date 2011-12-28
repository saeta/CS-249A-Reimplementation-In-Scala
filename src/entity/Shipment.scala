package entity
import query.ConnectQuery

// TODO: investigate possible problems with hashCode and ==
case class Shipment(fleet: Fleet, src: Location, dst: Location, size: Int) {
  /** Cached path to traverse. */
  var path: List[Segment] = fleet.connect(ConnectQuery(src, dst)).head.path
  println("Path for shipment dest: " + dst + " is: " + path)
  /** Gets next Segment to travel along. */
  def next = path.head
  /** Advances the Shipment along its route. */
  def moved(): Unit = {
    println(this + " moving.")
    path = path.tail
  }
  /** Updates stats in fleet. Called only when at destination. */
  // TODO: Shipment.completed() (Note: current impl has race conditions!)
  def completed(): Unit = fleet.completedShipments += 1
}
