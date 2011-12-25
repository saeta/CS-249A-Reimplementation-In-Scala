package adt

object Speed { 
  implicit def int2Speed(s: Int) = Speed(s)
}

case class Speed(spd: Int) extends Ordered[Speed] {
  require(spd > 0)
  def *(l: Length) = Time(spd * l.len)
  def +(s: Speed) = Speed(spd + s.spd)
  def compare(s: Speed) = spd - s.spd
}