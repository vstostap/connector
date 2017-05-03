package connector.api

import akka.http.scaladsl.server.Directives._


trait API {
  def routes =
    get {
      pathSingleSlash {
        getFromResource("web/index.html")
      }
    } ~
      getFromResourceDirectory("web")
}
