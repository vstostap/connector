### #connector

No readme yet.
Work in progress... 

### Technologies stack:

- Scala
- Node.js
- React.js
- Akka http (REST API)
- Express + Socket.io (Node app)
- Apache Spark
- Apache Kafka
- Cassandra
- Specs2
- Travis CI
- SBT
- GNU make

### Install

```
JVM, Scala, node, npm, apache zookeeper, GNU Make have to be installed.
```

Clone the repo:
```
$ git clone git@github.com:vstostap/connector.git
```

Compile all dependencies and main sources:
```
$ make build
```

Install all node dependencies:
```
$ make build-node
```

Build clien side with webpack:
```
$ make build-webpack
```

### Run

Start akka http + spark app:
```
$ make serve
```

Start node web app:
```
$ make node
```

### Test
Compiles and runs all tests.

```
$ make test
```

### Package
Creates a jar file containing the files in src/main/resources and the classes compiled from src/main/scala:
```
$ make package
```

### License
MIT