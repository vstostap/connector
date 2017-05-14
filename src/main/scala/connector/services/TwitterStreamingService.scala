package connector.services

import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.scaladsl.Flow
import connector.models.{Tweet, TweetTest}
import org.apache.spark.streaming.{Duration, Seconds}
import org.apache.spark.streaming.twitter._

import scala.concurrent.Future
import scala.io.Source


object TwitterStreamingService {

  private def loadTwitterKeys() = {
    val source = Source.fromURL(getClass.getResource("/twitter.conf"))
    try {
      val lines: Iterator[String] = source.getLines()
      val props = lines.map(line => line.split("=")).map { case (scala.Array(k, v)) => (k, v) }
      props.foreach {
        case (k: String, v: String) =>
          System.setProperty("twitter4j.oauth." + k, v)
      }
    }
    catch {
      case _: Exception => TextMessage("File with credentials is missing")
    }
    finally {
      source.close()
    }
  }

  loadTwitterKeys()

  /* Create a input stream that returns tweets received from Twitter. */
  def process(filters: String) = {

    val duration: Duration = Seconds(3600)

    val filtersSeq = filters.split(",")

    val ssc = SparkStreamingContextService.configureStreamingContext()

    val stream = TwitterUtils.createStream(ssc, None, filtersSeq)

    val hashTags = stream.flatMap(status => status.getText.split(" ").filter(_.startsWith("#")))

    val topHashTags = hashTags.map((_, 1))
      .reduceByKeyAndWindow(_ + _, duration)
      .map{case (topic, count) => (count, topic)}
      .transform(_.sortByKey(false))

    // take most popular hashtags
    topHashTags.foreachRDD(rdd => {
      val topList = rdd.take(10)
      println("\nPopular topics in last %s seconds (%s total):".format(duration, rdd.count()))
      topList.foreach{case (count, tag) => TextMessage("ECHO: tag - #" + tag + " count:" + count)}
    })

    ssc.start()
    ssc.awaitTermination()
    TextMessage("Stream is terminated...")
  }
}
