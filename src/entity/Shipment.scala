package entity
import query.ConnectQuery

case class Shipment(fleet: Fleet, src: Location, dst: Location, size: Int) {
  /** Cached path to traverse. */
  var path: List[Segment] = fleet.connect(ConnectQuery(src, dst)).head.path
  /** Gets next Segment to travel along. */
  def next = path.head
  /** Advances the Shipment along its route. */
  def moved(): Unit = path = path.tail
}
