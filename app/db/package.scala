import io.getquill.{ H2Dialect, JdbcContext, SnakeCase }

package object db {

  type DbContext = JdbcContext[H2Dialect, SnakeCase]

}
