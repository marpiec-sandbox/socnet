package pl.marpiec.cqrs

import java.sql.{DriverManager, Connection}
import io.Source
import java.io.{FileReader, BufferedReader}
import org.apache.commons.lang.StringUtils

/**
 * @author Marcin Pieciukiewicz
 */

class DatabaseInitializer(val connectionPool:DatabaseConnectionPool) {

  val FILE_NAME = "eventStore.sql"

  def initDatabase() {

    val source = Source.fromURL(getClass.getClassLoader.getResource(FILE_NAME))
    val lines = source.getLines

    lines.foreach((line: String) => {
      executeSqlCommand(line)
    });

    source.close()

  }

  private def executeSqlCommand(sqlCommand: String) {
    if (StringUtils.isNotBlank(sqlCommand)) {
      connectionPool.getConnection().prepareStatement(sqlCommand).execute
    }
  }
}
