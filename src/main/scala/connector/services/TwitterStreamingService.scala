package connector.services

import java.util.Calendar

import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.scaladsl.Flow
import connector.core.Config
import connector.kafka.KafkaProducer
import connector.models.{Tweet, TweetTest}
import org.apache.spark.streaming.{Duration, Seconds, StreamingContext}
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
  var ssc: StreamingContext = _

  /* Create an input stream that returns tweets received from Twitter. */
  def process(filters: String) = {

    ssc = SparkStreamingContextService.configureStreamingContext()

    val duration: Duration = Seconds(3600)

    val filtersSeq = filters.split(",")

    val producerApp = KafkaProducer(Config.kafkaBrokerHost + Config.kafkaBrokerPort, defaultTopic = Option(Config.kafkaTopic))

    val stream = TwitterUtils.createStream(ssc, None, filtersSeq)

    val hashTags = stream.flatMap(status => status.getText.split(" ").filter(_.startsWith("#")))

    val topHashTags = hashTags.map((_, 1))
      .reduceByKeyAndWindow(_ + _, duration)
      .map{case (topic, count) => (count, topic)}
      .transform(_.sortByKey(false))

    // take most popular hashtags
    topHashTags.foreachRDD(rdd => {
      val topList = rdd.take(5)
      println("\nPopular topics in last %s seconds (%s total):".format(duration, rdd.count()))
      producerApp.send(
        Calendar.getInstance().getTime.toString.toCharArray.map(_.toByte),
        rdd.count.toString.toCharArray.map(_.toByte),
        Option(Config.kafkaTopic))
      topList.foreach{case (count, tag) =>  producerApp.send(
        tag.toCharArray.map(_.toByte),
        count.toString.toCharArray.map(_.toByte),
        Option(Config.kafkaTopic))}
    })

    ssc.start()
    ssc.awaitTermination()
    TextMessage("Stream is terminated...")
  }

  def stop = {
    ssc.stop(stopSparkContext = false, stopGracefully = true)
    TextMessage("Stream is terminated...")
  }
}