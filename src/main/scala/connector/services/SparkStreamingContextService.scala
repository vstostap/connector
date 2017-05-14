package connector.services

import connector.core.Config
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SparkStreamingContextService {

  val REFRESH = 10; //refresh every val seconds

  def configureStreamingContext() = {
    implicit val sparkConf = new SparkConf().setMaster(Config.sparkMaster).setAppName(Config.sparkName)
    val sc = new SparkContext(sparkConf)
    new StreamingContext(sc, Seconds(REFRESH))
  }
}
