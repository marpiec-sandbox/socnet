package pl.marpiec.socnet.readdatabase.mock

import pl.marpiec.socnet.model.User
import pl.marpiec.cqrs.{Aggregate, DataStore}
import pl.marpiec.util.UID
import org.springframework.stereotype.Repository
import org.springframework.beans.factory.annotation.Autowired
import org.apache.commons.lang.StringUtils
import pl.marpiec.socnet.readdatabase.UserDatabase

/**
 * ...
 * @author Marcin Pieciukiewicz
 */


class UserDatabaseMockImpl @Autowired()(dataStore: DataStore)
  extends AbstractDatabase[User](dataStore) with UserDatabase {

  val EMAIL_INDEX: String = "email"


  startListeningToDataStore(dataStore, classOf[User])

  addIndex(EMAIL_INDEX, (aggregate: Aggregate) => {
    val user = aggregate.asInstanceOf[User]
    user.email
  });


  def addUser(user: User) {
    add(user)
  }

  def updateUser(user: User) {
    addOrUpdate(user)
  }

  def getUserByEmail(email: String): Option[User] = getByIndex(EMAIL_INDEX, email)

  def getUserById(id: UID): Option[User] = getById(id)

  def findUser(query: String): List[User] = {
    val all = getAll

    val queryWords = StringUtils.defaultString(query).split("\\s+")

    val filtered = (all.filter(user => {

      var matches = true

      queryWords.foreach(word => {
        matches = matches && (StringUtils.equals(word, user.firstName) || StringUtils.equals(word, user.lastName))
      })

      matches
    }))

    filtered
  }

  def getUsersByIds(ids: List[UID]): List[User] = {
    val filtered = (getAll.filter(user => {
      ids.contains(user.id)
    }))
    filtered
  }

  def getAllUsers = getAll
}
