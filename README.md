### #connector

No readme yet.
Work in progress... 

### Technologies stack:

- Scala
- React.js
- Akka http (REST API)
- Apache Spark
- Apache Mesos
- Apache Kafka
- Cassandra
- Specs2
- Travis CI
- SBT
- GNU make

### Install

Clone the repo:
```
$ git clone git@github.com:vstostap/connector.git
```

Compile all dependencies and main sources:
```
$ make build
```

### Run

Start the server:
```
$ make serve
```
That's it. Open [http://localhost:9000](http://localhost:9000) at your browser.

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