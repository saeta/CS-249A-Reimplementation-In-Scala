package entity
import query.ConnectQuery

// TODO: investigate possible problems with hashCode and ==
case class Shipment(fleet: Fleet, src: Location, dst: Location, size: Int) {
  /** Cached path to traverse. */
  var path: List[Segment] = fleet.connect(ConnectQuery(src, dst)).head.path
  /** Gets next Segment to travel along. */
  def next = path.head
  /** Advances the Shipment along its route. */
  def moved(): Unit = path = path.tail
  /** Updates stats in fleet. Called only when at destination. */
}
