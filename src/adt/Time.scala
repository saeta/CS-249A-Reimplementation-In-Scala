package adt

object Time {
  implicit def int2Time(t: Int) = Time(t)
}

case class Time(tm: Int) extends Ordered[Time] {
  require(tm > 0)
  def *(s: Speed) = Length(s.spd * tm)
  def +(t: Time) = Time(tm + t.tm)
  def -(t: Time) = Time(tm - t.tm)
  def compare(t: Time) = tm - t.tm
}
