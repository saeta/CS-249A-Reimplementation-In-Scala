package adt

object CostPerDistance {
  implicit def int2Cost(c: Int) = CostPerDistance(c)
}

case class CostPerDistance(c: Int) extends Ordered[CostPerDistance] {
  require(c > 0)
  def +(cst: CostPerDistance) = CostPerDistance(c + cst.c)
  def -(cst: CostPerDistance) = CostPerDistance(c - cst.c)
  def *(dst: ScaledLength) = Cost(c * dst.l)
  def compare(cst: CostPerDistance) = c - cst.c
}
