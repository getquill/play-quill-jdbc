import io.getquill.{ H2JdbcContext, SnakeCase }

package object db {

  type DbContext = H2JdbcContext[SnakeCase]

}
