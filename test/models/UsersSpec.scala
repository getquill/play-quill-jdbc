package models

import _root_.test._
import org.scalatest.Matchers._
import org.scalatest.{ TestData, WordSpec }
import org.scalatestplus.play.OneAppPerTest
import play.api._

class UsersSpec extends WordSpec with OneAppPerTest {

  override def newAppForTest(testData: TestData): Application = fakeApp

  "Users" should {
    "create and find" in {
      val users = app.injector.instanceOf(classOf[Users])
      val user = users.create(User(0L, "test1", true))
      user.id !== 0L
      val userFound = users.find(user.id)
      userFound shouldBe defined
      userFound.foreach(_.name shouldBe "test1")
    }


  }
}
