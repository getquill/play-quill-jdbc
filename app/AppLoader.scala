import java.io.Closeable
import javax.sql.DataSource

import controllers.UserController
import io.getquill._
import io.getquill.naming.SnakeCase
import io.getquill.sources.sql.idiom.H2Dialect
import play.api.ApplicationLoader.Context
import play.api._
import play.api.db.evolutions.Evolutions
import play.api.db.{DBComponents, HikariCPComponents}
import play.api.inject.{Injector, NewInstanceInjector, SimpleInjector}
import play.api.routing.Router
import play.api.routing.sird._
import services.{UserServices}

class AppLoader extends ApplicationLoader {
  override def load(context: Context): Application = new BuiltInComponentsFromContext(context) with DBComponents with HikariCPComponents {

    lazy val db = source {
      new JdbcSourceConfig[H2Dialect, SnakeCase]("default") {
        override def dataSource: DataSource with Closeable =
          dbApi.database(name).dataSource.asInstanceOf[DataSource with Closeable]
      }
    }

    lazy val userServices = new UserServices(db)
    lazy val userController = new UserController(userServices)

    val router = Router.from {
      case GET(p"/users/${long(id)}")    => userController.get(id)
      case POST(p"/users")               => userController.create
      case DELETE(p"/users/${long(id)}") => userController.delete(id)
      case PUT(p"/users/${long(id)}")    => userController.update(id)
    }

    override lazy val injector: Injector =
      new SimpleInjector(NewInstanceInjector) + userServices + router + cookieSigner + csrfTokenSigner + httpConfiguration + tempFileCreator + global

    Evolutions.applyEvolutions(dbApi.database("default"))

  }.application
}