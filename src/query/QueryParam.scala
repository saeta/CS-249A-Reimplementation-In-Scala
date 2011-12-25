package query

import adt._
import entity._

trait QueryParam {
  def apply(s: Segment):QueryParam
  def valid:Boolean
}

case class DistParam(dist: Length) extends QueryParam {
  def apply(s: Segment) = new DistParam(dist - s.len)
  def valid = dist >= 0
}
case class TimeParam(time: Time) extends QueryParam {
  def apply(s: Segment) = new TimeParam(time - s.time)
  def valid = time >= 0
}