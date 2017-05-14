package connector.services

import com.datastax.driver.core.{ProtocolOptions, Cluster}
import connector.core.Config

trait ClusterBuilder {
  def cluster: Cluster
}

object CassandraCluster {
  lazy val cluster: Cluster =
    Cluster.builder().
      addContactPoints(Config.cassandraHost).
      withCompression(ProtocolOptions.Compression.SNAPPY).
      withPort(Config.cassandraPort)
      .build()
}
