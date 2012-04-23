package pl.marpiec.cqrs

import java.sql.{DriverManager, Connection}
import io.Source
import pl.marpiec.util.Strings
import java.io.{FileReader, BufferedReader}

/**
 * @author Marcin Pieciukiewicz
 */

object DatabaseInitializer {

  val FILE_NAME = "eventStore.sql"

  private val connection: Connection = DriverManager.getConnection("jdbc:h2:~/test", "sa", "sa");

  def initDatabase() {

    val source = Source.fromURL(getClass.getClassLoader.getResource(FILE_NAME))
    val lines = source.getLines

    lines.foreach((line: String) => {
      executeSqlCommand(line)
    });

    source.close()
    connection.close()

  }

  private def executeSqlCommand(sqlCommand: String) {
    if (Strings.isNotBlank(sqlCommand)) {
      connection.prepareStatement(sqlCommand).execute
    }
  }
}
