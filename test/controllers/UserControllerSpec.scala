package controllers

import scala.util.Random

import org.scalatest.TestData
import org.scalatestplus.play.{ OneAppPerTest, PlaySpec }

import play.api.Application
import play.api.test.FakeRequest
import play.api.test.Helpers._

import io.getquill._
import io.getquill.naming.SnakeCase
import io.getquill.sources.sql.idiom.H2Dialect
import services.{ UserServices, User }
import test._

class UserControllerSpec extends PlaySpec with OneAppPerTest {

  override def newAppForTest(testData: TestData): Application = fakeApp

  lazy val db = source(new JdbcSourceConfig[H2Dialect, SnakeCase]("quill.default"))
  lazy val userServices = new UserServices(db)

  "GET /user/:id" should {
    "200 OK with JSON" in {
      val user = userServices.create(User(0, s"Name${Random.nextLong()}", true))
      val json = contentAsJson(route(app, FakeRequest(GET, s"/user/${user.id}")).get)
      (json \ "name").as[String] mustBe(user.name)
    }
  }

}
