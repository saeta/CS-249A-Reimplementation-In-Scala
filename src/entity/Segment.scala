package entity

import adt._

// Difficulty? Return Segment?
case class Segment(fleet: Fleet, src: Location, dst: Location, mode: Mode, 
    len: Length, difficulty: Difficulty = Difficulty(1)) {
  src.segs += this
  fleet.segs += this
  def time = len / fleet.speed(mode)
  def cost = len * difficulty * fleet.cost(mode)
  override def toString = src.name + "->" + dst.name
}