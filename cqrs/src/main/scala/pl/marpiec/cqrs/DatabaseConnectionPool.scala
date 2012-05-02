package pl.marpiec.cqrs

import java.sql.Connection

/**
 * @author Marcin Pieciukiewicz
 */

trait DatabaseConnectionPool {
  def getConnection():Connection
}
