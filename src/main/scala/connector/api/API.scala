package connector.api

import connector.routes._
import akka.http.scaladsl.server.Directives._


trait API {
  val routes = Twitter.routes ~ Resources.routes
}
