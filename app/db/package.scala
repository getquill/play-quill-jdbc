import io.getquill.{ H2Dialect, JdbcContext, SnakeCase }

package object db {

  type JdbcDatabase = JdbcContext[H2Dialect, SnakeCase]

}
