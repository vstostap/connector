package connector.controllers

import akka.http.scaladsl.model.ws._
import akka.stream.scaladsl.Flow
import connector.models.{Tweet, TweetTest}
import connector.services.TwitterStreamingService._

object Twitter {

  val processWithFilters: Flow[Message, Message, _] = {
    Flow[Message].map {
      case TextMessage.Strict(filters) => process(filters)
      case _ => TextMessage("Message type unsupported")
    }
  }

  val stopProcess: Flow[Message, Message, _] = {
    Flow[Message].map {
       _ => stop
    }
  }
}
