package es.upm.fi.oeg.morph.esper
import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import akka.actor.Props
import scala.util.{Try, Success, Failure}
import akka.pattern.{ ask, pipe }
import akka.util.Timeout
import concurrent.duration._
import scala.concurrent.Await
import language.postfixOps
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.slf4j.LoggerFactory
import org.scalatest.BeforeAndAfter
import akka.pattern.AskTimeoutException
import org.scalatest.BeforeAndAfterAll

class EsperServerTest  extends FlatSpec with Matchers with BeforeAndAfter with BeforeAndAfterAll {
  private val logger= LoggerFactory.getLogger(this.getClass)

  lazy val esper=new EsperServer
  implicit val timeout = Timeout(5 seconds) // needed for `?` below
  lazy val proxy=new EsperProxy(esper.system)

    
  override def beforeAll()={
    logger.info("the before part ===============================")
    esper.startup 
    import proxy.system.dispatcher
    proxy.system.scheduler.schedule(0 seconds, 1 seconds){
    proxy.engine ! Event("wunderground",Map("temperature"->9.4,"stationId"->"ABT08"))}
    logger.info("all sent ===============================")

  }
    
    
  "Esper query" should "get registered and receive results" in{
    //val client = proxy.system.actorOf(Props(new EsperClient(proxy.engine)), "lookupActor")

    proxy.engine ! CreateWindow("wunderground","wund","3")
        Thread.sleep(3000)

    val d=(proxy.engine ? ExecQuery("select * from wund"))
    //d.foreach(println)
    import proxy.system.dispatcher
    d onComplete {
      case Success(v)=>
        val list=v.asInstanceOf[Array[Object]]
        println("value "+list.mkString)
      case Failure(e)=>
        println("failed "+e.getMessage)
    }
    
    Thread.sleep(6000)
    println("wow")
    //client.shutdown
  }
  
  "non existing data pull" should "fail" in{
    val f=(proxy.engine ? PullData("nonexisting"))
    println(intercept[IllegalArgumentException]{Await.result(f,timeout.duration).asInstanceOf[String]})
  }
  
  override def afterAll()={
    
    logger.debug("dont kill me yet %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%")
    esper.shutdown
  }
}