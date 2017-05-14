package connector.services

import java.util.Properties

import connector.kafka.KafkaEmbedded
import org.I0Itec.zkclient.ZkClient

import kafka.utils.ZKStringSerializer
import kafka.admin.AdminUtils

import scala.concurrent.duration._

object KafkaZookeeper {

  var zookeeperEmbedded: Option[ZooKeeperEmbedded] = None
  var zkClient: Option[ZkClient] = None
  var kafkaEmbedded: Option[KafkaEmbedded] = None

  /**
    * Launches in-memory, embedded instances of ZooKeeper and Kafka
    */
  def startZooKeeperAndKafka(topic: String, numTopicPartitions: Int = 1, numTopicReplicationFactor: Int = 1,
                                     zookeeperPort: Int = 2181) {

    zookeeperEmbedded = Some(new ZooKeeperEmbedded(zookeeperPort))
    for {z <- zookeeperEmbedded} {
      val brokerConfig = new Properties
      brokerConfig.put("zookeeper.connect", z.connectString)
      kafkaEmbedded = Some(new KafkaEmbedded(brokerConfig))
      for {k <- kafkaEmbedded} {
        k.start()
      }

      val sessionTimeout = 30.seconds
      val connectionTimeout = 30.seconds
      zkClient = Some(new ZkClient(z.connectString, sessionTimeout.toMillis.toInt, connectionTimeout.toMillis.toInt,
        ZKStringSerializer))
      for {
        zc <- zkClient
      } {
        val topicConfig = new Properties
        AdminUtils.createTopic(zc, topic, numTopicPartitions, numTopicReplicationFactor, topicConfig)
      }
    }
  }

  def stopZooKeeperAndKafka() {
    for {k <- kafkaEmbedded} k.stop()
    for {zc <- zkClient} zc.close()
    for {z <- zookeeperEmbedded} z.stop()
  }
}
