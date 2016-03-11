import io.getquill.naming.SnakeCase
import io.getquill.sources.jdbc.JdbcSource
import io.getquill.sources.sql.idiom.H2Dialect

package object db {

  type JdbcDatabase = JdbcSource[H2Dialect, SnakeCase]

}
