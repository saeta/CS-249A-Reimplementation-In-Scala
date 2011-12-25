package query

import entity._

case class ExploreQuery(start: Location, constraints: Set[QueryParam]) {
  def valid = constraints.filterNot(_.valid).size == 0
  def apply(s: Segment): ExploreQuery = {
    ExploreQuery(s.dst, constraints.map(_(s)))
  }
}
