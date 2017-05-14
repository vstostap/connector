package connector.routes

import akka.http.scaladsl.server.Directives._

object Resources {

  val routes =
    get {
      pathSingleSlash {
        getFromResource("web/index.html")
      }
    } ~
      getFromResourceDirectory("web")
}
