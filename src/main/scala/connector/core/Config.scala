package connector.core

import com.typesafe.config.ConfigFactory

object Config {

  private val config = ConfigFactory.load()

  lazy val interface = config.getString("app.server.host")
  lazy val port = config.getInt("app.server.port")
  lazy val actorSystemName = config.getString("app.actor-system.name")

  lazy val cassandraPort = config.getInt("app.database.cassandra.port")
  lazy val cassandraHost = config.getString("app.database.cassandra.host")

}