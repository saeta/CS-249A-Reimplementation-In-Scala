package entity

import adt.Mode
import adt.Speed
import query._
import adt.CostPerDistance

class Fleet {
  import scala.collection.mutable.Map
  type Path = List[Segment]

  val speed = Map[Mode, Speed]().withDefaultValue(1)
  val capacity = Map[Mode, Int]().withDefaultValue(1)
  val cost = Map[Mode, CostPerDistance]().withDefaultValue(1)

  val segs = scala.collection.mutable.Set[Segment]()
  val locs = scala.collection.mutable.Set[Location]()
  
  def count(lt: LocType) = locs.count(loc => loc.lt == lt)
  def count(mode: Mode) = segs.count(s => s.mode == mode)
  def explore(query: ExploreQuery): Iterable[Path] = {
    def exploreRec(query: ExploreQuery, 
                   excludeSet: Set[Location]): Iterable[Path] = {
      if (query.valid) {
        val newExcludeSet = excludeSet + query.start
        val newPaths = for {
          s <- query.start.segs
          if !newExcludeSet.contains(s.dst)
          path <- exploreRec(query(s), newExcludeSet)
        } yield s :: path
        val basicPaths = for {
          s <- query.start.segs
          if !newExcludeSet.contains(s.dst)
        } yield List(s)
        newPaths ++ basicPaths
      } else List()
    }
    exploreRec(query, Set())
  }
  
  def connect(query: ConnectQuery): Iterable[ConnectPath] = {
    object Path // Must declare this, for some reason... :-(
    case class Path(p: List[Segment] = List(), s: Set[Location] = Set()) {
      def head = p.head.dst
      def contains(seg: Segment) = s.contains(seg.dst)
      def ::(seg: Segment) = new Path(seg :: p, s ++ List(seg.dst, seg.src))
      def reverse = p.reverse
    }
    val srch = scala.collection.mutable.Queue[Path]() // Paths in reversed order
    var buf = List[ConnectPath]()
    def expandLoc(loc: Location, route: Path) =
      for { s <- loc.segs if !route.contains(s) } srch.enqueue(s :: route)
    
    expandLoc(query.start, Path())
    while (!srch.isEmpty) {
      val soFar = srch.dequeue()
      if (soFar.head == query.end) buf ::= ConnectPath(soFar.reverse)
      else expandLoc(soFar.head, soFar)
    }
    buf
  }
}
