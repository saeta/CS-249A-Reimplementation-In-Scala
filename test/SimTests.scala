import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSuite

import scala.actors.Actor.{self, actor}

import adt._
import entity._
import query._
import sim._

@RunWith(classOf[JUnitRunner])
class SimTests extends FunSuite with BeforeAndAfter with ShouldMatchers {
  var fleet:Fleet = _
  var c1, c2, p1, p2: Location = _
  var c1_p1, p1_c1, p2_p1, p1_p2, c2_p2, p2_c2: Segment = _
  
  before {
    fleet = new Fleet
    c1 = Location(fleet, "c1", CUST)
    c2 = Location(fleet, "c2", CUST)
    p1 = Location(fleet, "p1", PORT)
    p2 = Location(fleet, "p2", PORT)
    c1.other = c2
    c2.other = c1
    
    c1_p1 = Segment(fleet, c1, p1, TRUCK, Length(4))
    p1_c1 = Segment(fleet, p1, c1, TRUCK, Length(4))
    p2_p1 = Segment(fleet, p2, p1, TRUCK, Length(4))
    p1_p2 = Segment(fleet, p1, p2, TRUCK, Length(4))
    c2_p2 = Segment(fleet, c2, p2, TRUCK, Length(4))
    p2_c2 = Segment(fleet, p2, c2, TRUCK, Length(4))    
  }
  
  test("Basic") {
    fleet.clock.stopTIme = 30
    fleet.clock ! Start(self)
    (self.receive { case DONE => true }) should be (true)
    fleet.completedShipments should be (12)
  }
  
  test("Restart") {
    fleet.clock.stopTIme = 10
    fleet.clock ! Start(self)
    (self.receive { case DONE => true}) should be (true)
    
    fleet.clock.stopTIme = 30
    fleet.clock ! Start(self)
    (self.receive { case DONE => true}) should be (true)
    fleet.completedShipments should be (12)
  }
}