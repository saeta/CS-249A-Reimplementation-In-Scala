import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers

import adt._
import entity._
import query._

@RunWith(classOf[JUnitRunner])
class IntegrationTests extends FunSuite with BeforeAndAfter with ShouldMatchers {
  var fleet = new Fleet
  var c1, c2, p1, p2: Location = _
  var c1_p1, p1_c1, p2_p1, p1_p2, c2_p2, p2_c2: Segment = _
  
  before {
    fleet = new Fleet
    c1 = Location(fleet, "c1", CUST)
    c2 = Location(fleet, "c2", CUST)
    p1 = Location(fleet, "p1", PORT)
    p2 = Location(fleet, "p2", PORT)
    
    c1_p1 = Segment(fleet, c1, p1, TRUCK, Length(4))
    p1_c1 = Segment(fleet, p1, c1, TRUCK, Length(4))
    p2_p1 = Segment(fleet, p2, p1, TRUCK, Length(4))
    p1_p2 = Segment(fleet, p1, p2, TRUCK, Length(4))
    c2_p2 = Segment(fleet, c2, p2, TRUCK, Length(4))
    p2_c2 = Segment(fleet, p2, c2, TRUCK, Length(4))    
  }
  
  test("Explore") {
	val explore = fleet.explore(ExploreQuery(c1, Set(DistParam(8))))
	explore should equal (Set(List(c1_p1, p1_p2, p2_c2), List(c1_p1, p1_p2), 
	                          List(c1_p1)))
  }
  
  test("Connect - Simple") {
    val conn = fleet.connect(ConnectQuery(c1, c2))
    conn should equal (List(ConnectPath(List(c1_p1, p1_p2, p2_c2))))
    val h = conn.head
    h.cost should equal (Cost(12))
  }
  
  test("Connect - MultiPath") {
    val c1_c2 = new Segment(fleet, c1, c2, TRUCK, Length(3))
    val conn = fleet.connect(ConnectQuery(c1, c2))
    conn should equal (List(ConnectPath(List(c1_p1, p1_p2, p2_c2)), 
        ConnectPath(List(c1_c2))))
  }
}
