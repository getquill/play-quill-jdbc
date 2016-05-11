package services

import io.getquill._
import db.JdbcDatabase

case class User (id: Long, name: String, isActive: Boolean)

class UserServices (db: JdbcDatabase) {

  val users = quote(query[User].schema(_.entity("users").generated(_.id)))

  def find (id: Long) =
    db.run(users.filter(c => c.id == lift(id) && c.isActive)).headOption

  def create (user: User) = {
    val newId = db.run(users.insert)(List(user))
    user.copy(id = newId.head)
  }

  def delete (user: User) = {
    db.run(users.filter(_.id == lift(user.id)).delete)
  }

  def update (user: User) = {
    db.run(users.filter(_.id == lift(user.id)).update(_.name -> lift(user.name), _.isActive -> lift(user.isActive)))
  }

}
