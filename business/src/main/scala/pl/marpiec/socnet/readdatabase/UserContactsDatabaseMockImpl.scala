package pl.marpiec.socnet.readdatabase

import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.cqrs.{Aggregate, DataStore}
import pl.marpiec.socnet.model.UserContacts
import pl.marpiec.util.UID
import org.springframework.stereotype.Repository

/**
 * @author Marcin Pieciukiewicz
 */

@Repository("userContactsDatabase")
class UserContactsDatabaseMockImpl @Autowired()(dataStore: DataStore) extends AbstractDatabase[UserContacts](dataStore) with UserContactsDatabase {

  val USER_ID_INDEX = "userId"

  startListeningToDataStore(dataStore, classOf[UserContacts])

  addIndex(USER_ID_INDEX, (aggregate: Aggregate) => {
    val userContacts = aggregate.asInstanceOf[UserContacts]
    userContacts.userId
  });

  def getUserContactsByUserId(userId: UID) = getByIndex(USER_ID_INDEX, userId)

  def getUserContactsIdByUserId(userId: UID) = {
    val userContactsOption = getByIndex(USER_ID_INDEX, userId)
    if (userContactsOption.isDefined) {
      Option(userContactsOption.get.id)
    } else {
      None
    }
  }
}
