package connector.utils

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._
import connector.models.{TweetTest, TweetsTest}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val tweetFormat = jsonFormat2(TweetTest)
  implicit val tweetsFormat = jsonFormat1(TweetsTest) // contains List[Tweets]
}