import controllers.UserController
import io.getquill._
import io.getquill.naming.SnakeCase
import io.getquill.sources.sql.idiom.{H2Dialect}
import play.api.ApplicationLoader.Context
import play.api._
import play.api.db.{HikariCPComponents, DBComponents}
import play.api.routing.Router
import play.api.routing.sird._
import play.api.db.evolutions.Evolutions


import services.{UserServices, AtomicCounter}

class AppLoader extends ApplicationLoader {
  override def load(context: Context): Application = new BuiltInComponentsFromContext(context) with DBComponents with HikariCPComponents {

    lazy val counter = new AtomicCounter
    lazy val countController = new controllers.CountController(counter)
    lazy val db = source(new JdbcSourceConfig[H2Dialect, SnakeCase]("quill.default"))
    lazy val userServices = new UserServices(db)
    private val userController = new UserController(userServices)


    val router = Router.from {
      case GET(p"/count")             => countController.count
      case GET(p"/user/${long(id)}")  => userController.get(id)
      case POST(p"/user")            => userController.create
    }

    Evolutions.applyEvolutions(dbApi.database("default"))
    dbApi.database("default").shutdown()
  }.application
}