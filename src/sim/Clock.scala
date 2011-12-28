package sim
import scala.actors.Actor
import scala.actors.Actor.self
import entity.Fleet
import entity.CUST

case class Ping(time: Int)
case class Pong(time: Int, from: Actor)

case class WorkItem(time: Int, msg: Any, target: Actor)
case class AfterDelay(delay: Int, msg: Any, target: Actor)

case class Start(main: Actor)
case object Done

/**
 * Clock object to be used in virtual-time simulation of the shipping network.
 * 
 * Inspiration for this has been taken from Programming In Scala, by Martin
 * Odersky, 1st edition, available on the web at: 
 * http://www.artima.com/pins1ed/actors-and-concurrency.html
 * 
 */
class Clock(fleet: Fleet, verbose: Boolean = false) extends Actor {
  private var running = false
  private var currentTime = 0
  private var main: Actor = _
  private var agenda: List[WorkItem] = List()
  private var busySimulants: Set[Actor] = Set()
  
  var stopTIme = 0
  
  def log(msg: => String) = if (verbose) println(msg) // Hacky logging.
  def stillRunning = running
  def allSimulants: Iterable[Actor] = 
    fleet.segs ++ fleet.schd
    
  def act() {
    loop {
      if (running && busySimulants.isEmpty) advance()
      reactToOneMessage()
    }
  }
  
  def advance() {
    if (currentTime >= stopTIme) {
      log("** Clock pausing at time " + currentTime + ".")
      main ! Done
      return
    }
    currentTime += 1
    log("Advancing to time " + currentTime)
    processCurrentEvents()
    for (sim <- allSimulants) sim ! Ping(currentTime)
    busySimulants = Set.empty ++ allSimulants
  }
  
  def processCurrentEvents() {
    val todoNow = agenda.takeWhile(_.time <= currentTime)
    agenda = agenda.drop(todoNow.length)
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
        
      case Start(m) =>
        running = true
        main = m
      
    }
  }
  
  def insert(ag: List[WorkItem], item: WorkItem): List[WorkItem] = {
    if (ag.isEmpty || item.time < ag.head.time) item :: ag
    else ag.head :: insert(ag.tail, item)
  }
  
  start()
}