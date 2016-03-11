package services

import _root_.test._
import io.getquill._
import io.getquill.naming.SnakeCase
import io.getquill.sources.sql.idiom.H2Dialect
import org.scalatest.Matchers._
import org.scalatest.{TestData, WordSpec}
import org.scalatestplus.play.OneAppPerTest
import play.api._

class UserServicesSpec extends WordSpec with OneAppPerTest {

  override def newAppForTest(testData: TestData): Application = fakeApp

  lazy val db = source(new JdbcSourceConfig[H2Dialect, SnakeCase]("quill.default"))
  lazy val userServices = new UserServices(db)

  "UserServices" should {
    "create and find" in {
      val user = userServices.create(User(0L, "test1", true))
      user.id !== 0L
      val userFound = userServices.find(user.id)
      userFound shouldBe defined
      userFound.foreach(_.name shouldBe "test1")
    }
  }
}
