import sbt.Keys._

name := "connector"
version := "0.0.1"
scalaVersion := "2.11.8"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

scalacOptions in Test ++= Seq("-Yrangepos")

libraryDependencies ++= {
  val sparkVersion = "2.0.0"
  Seq(
    "org.apache.spark"       %%  "spark-core"                % sparkVersion,
    "org.apache.spark"       %%  "spark-streaming"           % sparkVersion,
    "org.apache.bahir"       %%  "spark-streaming-twitter"   % sparkVersion
  )
}

libraryDependencies ++= {
  val akkaV       = "2.4.14"
  val akkaHttpV   = "10.0.0"
  Seq(
    "com.typesafe.akka"     %% "akka-actor"                 % akkaV,
    "com.typesafe.akka"     %% "akka-stream"                % akkaV,
    "com.typesafe.akka"     %% "akka-slf4j"                 % akkaV,
    "com.typesafe.akka"     %% "akka-testkit"               % akkaV,
    "com.typesafe.akka"     %% "akka-http-core"             % akkaHttpV,
    "com.typesafe.akka"     %% "akka-http"                  % akkaHttpV,
    "com.typesafe.akka"     %% "akka-http-testkit"          % akkaHttpV   % "test",
    "com.typesafe.akka"     %% "akka-http-spray-json"       % akkaHttpV
  )
}

libraryDependencies ++= {
  val mesosV = "1.1.0"
  Seq(
    "org.apache.mesos"      % "mesos"                      % mesosV
  )
}

libraryDependencies ++= {
  val kafkaV = "0.8.1"
  Seq(
    "org.apache.kafka"      % "kafka_2.10"                  % kafkaV
      exclude("javax.jms", "jms")
      exclude("com.sun.jdmk", "jmxtools")
      exclude("com.sun.jmx", "jmxri")
  )
}

libraryDependencies ++= {
  val cassandraV = "2.0.0"
  Seq(
    "com.datastax.cassandra" % "cassandra-driver-core"      % cassandraV
  )
}

libraryDependencies ++= {
  val specs2V = "3.8.5"
  Seq(
    "org.specs2"            %% "specs2-core"                % specs2V    % "test"
  )
}

libraryDependencies ++= {
  val java_wsV = "1.3.0"
  Seq(
  "org.java-websocket" % "Java-WebSocket" % java_wsV
  )
}