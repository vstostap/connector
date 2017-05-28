package connector.core

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.util.Timeout
import akka.stream.ActorMaterializer

import scala.concurrent.duration._
import scala.util.{Failure, Success}
import connector.api.API
import connector.services._


object Boot extends App with API {

  implicit val system = ActorSystem(Config.actorSystemName)
  implicit val timeout = Timeout(15.seconds)
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  KafkaZookeeper.startZooKeeperAndKafka(Config.kafkaTopic)

  val binding = Http().bindAndHandle(routes, Config.interface, Config.port)
  binding.onComplete {
    case Success(binding) ⇒
      val localAddress = binding.localAddress
      println(s"Server is listening on ${localAddress.getHostName}:${localAddress.getPort}")
    case Failure(e) ⇒
      println(s"Binding failed with ${e.getMessage}")
      system.terminate()
  }
}