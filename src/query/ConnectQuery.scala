package query

import entity._

case class ConnectQuery(start: Location, end: Location)
case class ConnectPath(path: List[Segment]) {
  def cost = path.map(_.cost).reduceLeft(_+_)
  def time = path.map(_.time).reduceLeft(_+_)
  def expedite = false
  override def toString = "" + cost + " " + time + " " + expedite + "; " + path
}
