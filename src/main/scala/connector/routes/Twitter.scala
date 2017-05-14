package connector.routes

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.{Directives, Route}
import akka.http.scaladsl.model.ws._
import akka.http.scaladsl.model.ws.UpgradeToWebSocket
import akka.stream.scaladsl.Flow

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}
import connector.controllers.Twitter._
import connector.models.{Tweet, TweetTest, TweetsTest}
import connector.utils.JsonSupport


object Twitter extends Directives with JsonSupport {

  private val pathName = "twitter"

  val routes =
    get {
      pathPrefix(pathName) {
          handleWebSocketMessages(processWithFilters)
        }
      }
}

/*
parameters('filters) { (filters: String) =>
Future[Option[List[TweetTest]]]
onComplete(data) {
  case Success (Some (items) ) => complete (TweetsTest (items) )
  case Success (None) => complete (StatusCodes.NotFound)
  case Failure (e) => complete (StatusCodes.InternalServerError)
} */