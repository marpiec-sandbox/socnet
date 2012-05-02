package pl.marpiec.cqrs

import org.apache.commons.pool.impl.GenericObjectPool
import org.apache.commons.dbcp.{PoolingDataSource, PoolableConnectionFactory, DriverManagerConnectionFactory, ConnectionFactory}
import org.apache.commons.pool.{PoolableObjectFactory, ObjectPool}
import javax.sql.DataSource


/**
 * @author Marcin Pieciukiewicz
 */

class DatabaseConnectionPoolImpl extends DatabaseConnectionPool {

  val datasource = initDataSource

  def initDataSource:DataSource = {
    val connectionFactory: ConnectionFactory = new DriverManagerConnectionFactory("jdbc:h2:~/test", "sa", "sa");
    val connectionPool:ObjectPool = new GenericObjectPool(null);

    val poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, connectionPool,null,null,false,true);

    new PoolingDataSource(connectionPool)
  }

  def getConnection() = datasource.getConnection()
}
