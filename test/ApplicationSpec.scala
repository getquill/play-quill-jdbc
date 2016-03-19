import org.scalatest.TestData
import org.scalatestplus.play._
import play.api.Application
import play.api.test._
import play.api.test.Helpers._
import test._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class ApplicationSpec extends PlaySpec with OneAppPerTest {

  override def newAppForTest(testData: TestData): Application = fakeApp

  "Routes" should {

    "send 404 on a bad request" in  {
      route(app, FakeRequest(GET, "/boum")).map(status(_)) mustBe Some(NOT_FOUND)
    }

  }
}
