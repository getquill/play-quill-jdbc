package controllers

import scala.util.Try

import play.api.libs.json._
import play.api.mvc.{ Action, Controller }

import services.UserServices
import services.User
import play.api.mvc._

class UserController (userServices: UserServices) extends Controller {

  implicit val userFormatter: OFormat[User] = Json.format[User]

  def get(id: Long) = Action { request =>
    userServices.find(id) match {
      case None => NotFound
      case Some(user) => Ok(Json.toJson(user))
    }
  }

  def create = Action (parse.json) { request=>
    Json.fromJson[User](request.body).fold(
      invalid => BadRequest,
      user => Ok(Json.toJson(userServices.create(user)))
    )
  }

  def delete(id: Long) = Action { request =>
    userServices.find(id) match {
      case None => NotFound
      case Some(user) =>
        userServices.delete(user)
        Ok
    }
  }

  def inactivate(id: Long) = Action { request =>
    userServices.find(id) match {
      case None => NotFound
      case Some(user) =>
        userServices.inactivate(user)
        Ok
    }
  }

  def activate(id: Long) = Action { request =>
    userServices.find(id) match {
      case None => NotFound
      case Some(user) =>
        userServices.activate(user)
        Ok
    }

  }

}
