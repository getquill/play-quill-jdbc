package controllers

import org.scalatest.TestData
import org.scalatestplus.play.{OneAppPerTest, PlaySpec}
import play.api.Application
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers._
import models.{User, Users}
import test._

import scala.util.Random

class UsersControllerSpec extends PlaySpec with OneAppPerTest {

  override def newAppForTest(testData: TestData): Application = fakeApp

  "GET /users/:id" should {
    "return 200 OK with body" in {
      val users = app.injector.instanceOf(classOf[Users])
      val name = s"Name${Random.nextLong()}"
      val user = users.create(User(0, name, true))
      val response = route(app, FakeRequest(GET, s"/users/${user.id}")).get
      status(response) mustBe OK
      val json = contentAsJson(response)
      (json \ "name").as[String] mustBe user.name
    }
  }

  "POST /users" should {
    "return 201 Created with Location header with created resource" in {
      val name = s"Name${Random.nextLong()}"
      val userJson = Json.obj("name" -> name, "isActive" -> true)
      val responseCreated = route(app, FakeRequest(POST, "/users").withJsonBody(userJson)).get
      status(responseCreated) mustBe CREATED
      val location = headers(responseCreated).get(LOCATION).get
      val responseGet = route(app, FakeRequest(GET, location)).get
      val json = contentAsJson(responseGet)
      (json \ "name").as[String] mustBe name
    }
  }

  "DELETE /users/:id" should {
    "return 204 No Content and delete resource" in {
      val users = app.injector.instanceOf(classOf[Users])
      val name = s"Name${Random.nextLong()}"
      val user = users.create(User(0, name, true))
      val response = route(app, FakeRequest(DELETE, s"/users/${user.id}")).get
      status(response) mustBe NO_CONTENT
      users.find(user.id) mustBe empty
    }
  }

  "PUT /users/:id" should {
    "return 204 No Content and update resource" in {
      val users = app.injector.instanceOf(classOf[Users])
      val name = s"Name${Random.nextLong()}"
      val user = users.create(User(0, name, true))
      val updatedName = s"Name${Random.nextLong()}"
      val updateUserJson = Json.obj("name" -> updatedName, "isActive" -> true)
      val response = route(app, FakeRequest(PUT, s"/users/${user.id}").withJsonBody(updateUserJson)).get
      status(response) mustBe NO_CONTENT
      val updatedUser = users.find(user.id)
      updatedUser.get.name mustBe updatedName
    }
  }

}
