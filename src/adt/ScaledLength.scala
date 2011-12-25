package adt

case class ScaledLength(l: Int) {
  def *(c: CostPerDistance) = Cost(l * c.c)
}