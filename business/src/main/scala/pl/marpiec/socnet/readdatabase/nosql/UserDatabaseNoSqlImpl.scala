package pl.marpiec.socnet.readdatabase.nosql

import pl.marpiec.socnet.readdatabase.UserDatabase
import pl.marpiec.util.UID
import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.cqrs.{Aggregate, DataStoreListener, DataStore}
import pl.marpiec.socnet.model.User
import pl.marpiec.socnet.mongodb.DatabaseConnectorImpl
import com.mongodb.QueryBuilder
import org.springframework.stereotype.Repository


/**
 * @author Marcin Pieciukiewicz
 */

@Repository("userDatabase")
class UserDatabaseNoSqlImpl @Autowired()(dataStore: DataStore)
  extends DataStoreListener with UserDatabase {

  val connector = new DatabaseConnectorImpl("users")

  startListeningToDataStore(dataStore, classOf[User])


  def addUser(user: User) {
    connector.insertAggregate(user)
  }

  def updateUser(user: User) {
    connector.insertAggregate(user)
  }

  def getUserById(id: UID): Option[User] = {
    connector.getAggregateById(id, classOf[User])
  }

  def getUsersByIds(ids: List[UID]): List[User] = {
    connector.getMultipleAggregatesByIds(ids, classOf[User])
  }

  def getAllUsers: List[User] = {
    connector.getAllAggregates(classOf[User])
  }

  def onEntityChanged(entity: Aggregate) = connector.insertAggregate(entity.asInstanceOf[User])


  def findUser(query: String): List[User] = {
    connector.findMultipleAggregatesByQuery(QueryBuilder.start("firstName").is(query).get(), classOf[User])
  }

  def getUserByEmail(email: String): Option[User] = {
    connector.findAggregateByQuery(QueryBuilder.start("email").is(email).get(), classOf[User])
  }


}
