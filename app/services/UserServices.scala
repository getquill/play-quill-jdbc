package services

import io.getquill._
import db.JdbcDatabase

case class User(id: Long, name: String, isActive: Boolean)

class UserServices(db: JdbcDatabase) {

  val users = quote(query[User](_.entity("users").generated(_.id)))

  def find(id: Long) =
    db.run {
      quote {
        (id: Long) => users.filter(c => c.id == id && c.isActive)
      }
    }(id).headOption


  def create(user: User) = {
    val newId = db.run {
      quote {
        users.insert
      }
    }(user)
    user.copy(id = newId)
  }

  def delete(user: User) = {
    db.run {
      quote {
        (id: Long) => users.filter(_.id == id).delete
      }
    }(user.id)
  }

  def inactivate(user: User) = {
    db.run {
      quote {
        (id: Long) => users.filter(_.id == id).update(_.isActive -> false)
      }
    }(user.id)
  }

  def activate(user: User) = {
    db.run {
      quote {
        (id: Long) => users.filter(_.id == id).update(_.isActive -> true)
      }
    }(user.id)
  }
}
