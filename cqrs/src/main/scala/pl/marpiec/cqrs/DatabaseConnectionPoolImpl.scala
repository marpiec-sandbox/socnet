package pl.marpiec.cqrs

import org.apache.commons.pool.impl.GenericObjectPool
import org.apache.commons.dbcp.{PoolingDataSource, PoolableConnectionFactory, DriverManagerConnectionFactory, ConnectionFactory}
import org.apache.commons.pool.{PoolableObjectFactory, ObjectPool}
import javax.sql.DataSource
import javax.naming.{NoInitialContextException, InitialContext}


/**
 * @author Marcin Pieciukiewicz
 */

class DatabaseConnectionPoolImpl extends DatabaseConnectionPool {

  val datasource = initDataSource

  def initDataSource:DataSource = {

    try {
      val cxt = new InitialContext()
      val ds = cxt.lookup( "java:/comp/env/jdbc/socnetDB" ).asInstanceOf[DataSource]
      if ( ds == null ) {
        throw new Exception("Data source not found!");
      }
      ds
    } catch {
      case ex:NoInitialContextException => {
        null //its test if there's no context - so ignore
      }
    }

  }

  def getConnection() = datasource.getConnection()
}
