package adt

object Difficulty {
  implicit def int2Difficulty(d: Int) = Difficulty(d)
}

case class Difficulty(d: Int) {
  def *(l: Length) = ScaledLength(d * l.len)
}