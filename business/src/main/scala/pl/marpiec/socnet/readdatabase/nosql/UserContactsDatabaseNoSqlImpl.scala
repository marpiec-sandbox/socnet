package pl.marpiec.socnet.readdatabase.nosql

import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.socnet.readdatabase.UserContactsDatabase
import org.springframework.stereotype.Repository
import pl.marpiec.cqrs.{DataStoreListener, DataStore}
import pl.marpiec.util.UID
import pl.marpiec.socnet.mongodb.DatabaseConnectorImpl
import com.mongodb.QueryBuilder
import pl.marpiec.socnet.model.UserContacts

/**
 * @author Marcin Pieciukiewicz
 */

@Repository("userContactsDatabase")
class UserContactsDatabaseNoSqlImpl @Autowired()(dataStore: DataStore)
  extends DataStoreListener[UserContacts] with UserContactsDatabase {

  val connector = new DatabaseConnectorImpl("usersContacts")

  startListeningToDataStore(dataStore, classOf[UserContacts])

  def getUserContactsByUserId(id: UID) = {
    connector.findAggregateByQuery(QueryBuilder.start("userId").is(id.uid).get(), classOf[UserContacts])
  }

  def getUserContactsIdByUserId(id: UID): Option[UID] = {
    //TODO optimize this

    val contactsOption: Option[UserContacts] = connector.findAggregateByQuery(QueryBuilder.start("userId").is(id.uid).get(), classOf[UserContacts])

    if (contactsOption.isDefined) {
      Option(contactsOption.get.id)
    } else {
      None
    }
  }

  def onEntityChanged(userContacts: UserContacts) = {
    connector.insertAggregate(userContacts)
  }
}
