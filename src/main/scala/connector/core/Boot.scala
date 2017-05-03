package connector.core

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.util.Timeout
import akka.stream.ActorMaterializer
import scala.concurrent.duration._
import scala.util.{ Success, Failure }

import connector.api.API


object Boot extends App with API{

  implicit val system = ActorSystem(Config.actorSystemName)
  implicit val timeout = Timeout(15.seconds)
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

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