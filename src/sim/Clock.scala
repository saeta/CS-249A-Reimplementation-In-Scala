package sim
import scala.actors.Actor
import scala.actors.Actor.self
import entity.Fleet
import entity.CUST

case class Ping(time: Int)
case class Pong(time: Int, from: Actor)

case class WorkItem(time: Int, msg: Any, target: Actor)
case class AfterDelay(delay: Int, msg: Any, target: Actor)

case object Start
case object Stop

/**
 * Clock object to be used in virtual-time simulation of the shipping network.
 * 
 * Inspiration for this has been taken from Programming In Scala, by Martin
 * Odersky, 1st edition, available on the web at: 
 * http://www.artima.com/pins1ed/actors-and-concurrency.html
 * 
 */
class Clock(fleet: Fleet) extends Actor {
  private var running = false
  private var currentTime = 0
  private var agenda: List[WorkItem] = List()
  private var busySimulants: Set[Actor] = Set()
  
  def allSimulants: Iterable[Actor] = 
    fleet.segs ++ fleet.locs.filter(_.lt == CUST)
  def act() {
    loop {
      if (running && busySimulants.isEmpty) advance()
      reactToOneMessage()
    }
  }
  
  def advance() {
    if (agenda.isEmpty && currentTime > 0) {
      println("** Agenda empty. Clock exiting at time " + currentTime + ".")
      self ! Stop
      return
    }
    currentTime += 1
    println("Advancing to time " + currentTime)
    
    processCurrentEvents()
    for (sim <- allSimulants) sim ! Ping(currentTime)
    busySimulants = Set.empty ++ allSimulants
  }
  
  def processCurrentEvents() {
    val todoNow = agenda.takeWhile(_.time <= currentTime)
    agenda.drop(todoNow.length)
    for (WorkItem(time, msg, target) <- todoNow) {
      assert(time == currentTime)
      target ! msg
    }
  }
  
  def reactToOneMessage() {
    react {
      case AfterDelay(delay, msg, target) =>
        val item = WorkItem(currentTime + delay, msg, target)
        agenda = insert(agenda, item)
        
      case item: WorkItem => agenda = insert(agenda, item)
        
      case Pong(time, sim) =>
        assert(time == currentTime)
        assert(busySimulants contains sim)
        busySimulants -= sim
        
      case Start => running = true
      
      case Stop =>
        for (sim <- allSimulants)
          sim ! Stop
        exit()
        
    }
  }
  
  def insert(ag: List[WorkItem], item: WorkItem): List[WorkItem] = {
    if (ag.isEmpty || item.time < ag.head.time) item :: ag
    else ag.head :: insert(ag.tail, item)
  }
}