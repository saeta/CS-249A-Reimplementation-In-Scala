import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSuite

import adt._
import entity._
import query._

@RunWith(classOf[JUnitRunner])
class TimeTest extends FunSuite with ShouldMatchers with BeforeAndAfter {
  var fleet: Fleet = _
  var src, dst: Location = _
  var forward, backward: Segment = _
  
  before {
    fleet = new Fleet
    fleet.speed(TRUCK) = 2
    src = Location(fleet, "a", PORT)
    dst = Location(fleet, "b", PORT)
  }
  
  test("Simple") {
    val forward = Segment(fleet, src, dst, TRUCK, Length(4))
    forward.time should equal (Time(2))
    val backward = Segment(fleet, dst, src, TRUCK, Length(16))
    backward.time should equal (Time(8))
  }
  
  test("Zero") {
    val testZero = Segment(fleet, src, dst, TRUCK, Length(1))
    testZero.time should equal (Time(1))    
  }
}