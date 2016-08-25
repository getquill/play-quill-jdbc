import java.io.Closeable
import javax.sql.DataSource

import controllers.UsersController
import io.getquill._
import play.api.ApplicationLoader.Context
import play.api._
import play.api.db.evolutions.Evolutions
import play.api.db.{DBComponents, HikariCPComponents}
import play.api.inject.{Injector, NewInstanceInjector, SimpleInjector}
import play.api.routing.Router
import play.api.routing.sird._
import models.{Users}

class AppLoader extends ApplicationLoader {
  override def load(context: Context): Application = new BuiltInComponentsFromContext(context) with DBComponents with HikariCPComponents {

    lazy val db = new JdbcContext[H2Dialect, SnakeCase](dbApi.database("default").dataSource.asInstanceOf[DataSource with Closeable])


    lazy val users = new Users(db)
    lazy val usersController = new UsersController(users)

    val router = Router.from {
      case GET(p"/users/${long(id)}")    => usersController.get(id)
      case POST(p"/users")               => usersController.create
      case DELETE(p"/users/${long(id)}") => usersController.delete(id)
      case PUT(p"/users/${long(id)}")    => usersController.update(id)
    }

    override lazy val injector: Injector =
      new SimpleInjector(NewInstanceInjector) + users + router + cookieSigner + csrfTokenSigner + httpConfiguration + tempFileCreator + global

    Evolutions.applyEvolutions(dbApi.database("default"))

  }.application
}