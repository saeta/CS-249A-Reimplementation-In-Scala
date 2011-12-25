package adt

object Cost {
  implicit def int2ActualCost(c: Int) = Cost(c) 
}

case class Cost(c: Int) extends Ordered[Cost] {
  require(c > 0)
  def compare(cst: Cost) = c - cst.c
  def -(o: Cost) = Cost(c - o.c)
  def +(o: Cost) = Cost(c + o.c)
}