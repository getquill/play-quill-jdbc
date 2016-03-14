import java.io.Closeable

import javax.sql.DataSource

import play.api.inject.{ NewInstanceInjector, SimpleInjector, Injector }

import controllers.UserController
import io.getquill._
import io.getquill.naming.SnakeCase
import io.getquill.sources.jdbc.JdbcSource
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

    lazy val db = source {
      new JdbcSourceConfig[H2Dialect, SnakeCase]("default"){
        override def dataSource: DataSource with Closeable = dbApi.database(name).dataSource.asInstanceOf[DataSource with Closeable]
      }
    }


    lazy val userServices = new UserServices(db)
    lazy val userController = new UserController(userServices)

    val router = Router.from {
      case GET(p"/count")             => countController.count
      case GET(p"/user/${long(id)}")  => userController.get(id)
      case POST(p"/user")            => userController.create
    }

    override lazy val injector: Injector = new SimpleInjector(NewInstanceInjector) + userServices + router + cookieSigner + csrfTokenSigner + httpConfiguration + tempFileCreator + global

    Evolutions.applyEvolutions(dbApi.database("default"))

  }.application
}