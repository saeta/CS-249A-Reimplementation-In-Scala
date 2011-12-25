package adt

object Length {
  implicit def int2Length(l: Int) = Length(l)
}

case class Length(len: Int) extends Ordered[Length] {
  def +(l: Length) = Length(len + l.len)
  def -(l: Length) = Length(len - l.len)
  def /(s: Speed) = Time(math.max(len / s.spd, 1))
  def *(d: Difficulty) = ScaledLength(len * d.d)
  def compare(l: Length) = len - l.len
}