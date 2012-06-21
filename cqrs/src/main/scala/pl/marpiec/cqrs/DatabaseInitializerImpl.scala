package pl.marpiec.cqrs

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import io.Source
import org.apache.commons.lang.StringUtils

/**
 * @author Marcin Pieciukiewicz
 */

@Service("databaseInitializer")
class DatabaseInitializerImpl @Autowired()(val jdbcTemplate: JdbcTemplate) extends DatabaseInitializer {

  val FILE_NAME = "eventStore.sql"

  def initDatabase() {

    val source = Source.fromURL(getClass.getClassLoader.getResource(FILE_NAME))
    val lines = source.getLines()

    lines.foreach((line: String) => {
      executeSqlCommand(line)
    });

    source.close()

  }

  private def executeSqlCommand(sqlCommand: String) {
    if (StringUtils.isNotBlank(sqlCommand)) {
      jdbcTemplate.update(sqlCommand)
    }
  }
}
