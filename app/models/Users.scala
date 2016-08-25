package models

import db.DbContext

case class User (id: Long, name: String, isActive: Boolean)

class Users(val db: DbContext) {
  import db._

  val users = quote(query[User].schema(_.entity("users")))

  def find(id: Long) = run(users.filter(c => c.id == lift(id) && c.isActive)).headOption

  def create(user: User) = user.copy(id = run(users.insert(lift(user)).returning(_.id)))

  def delete(user: User) = run(users.filter(_.id == lift(user.id)).delete)

  def update(user: User) = run(users.filter(_.id == lift(user.id)).update(lift(user)))

}
