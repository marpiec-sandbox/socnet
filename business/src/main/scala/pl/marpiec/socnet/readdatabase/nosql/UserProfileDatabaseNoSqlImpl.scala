package pl.marpiec.socnet.readdatabase.nosql

import org.springframework.stereotype.Repository
import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.cqrs.{DataStoreListener, DataStore}
import pl.marpiec.util.UID
import pl.marpiec.socnet.mongodb.DatabaseConnectorImpl
import pl.marpiec.socnet.model.{UserProfile, User}
import scala.Predef._
import com.mongodb.BasicDBObject
import pl.marpiec.socnet.readdatabase.UserProfileDatabase

/**
 * @author Marcin Pieciukiewicz
 */

@Repository("userProfileDatabase")
class UserProfileDatabaseNoSqlImpl @Autowired()(dataStore: DataStore)
  extends DataStoreListener[UserProfile] with UserProfileDatabase {

  val connector = new DatabaseConnectorImpl("usersProfiles")

  startListeningToDataStore(dataStore, classOf[UserProfile])

  def addUserProfile(userProfile: UserProfile) {
    connector.insertAggregate(userProfile)
  }

  def updateUserProfile(userProfile: UserProfile) {
    connector.insertAggregate(userProfile)
  }

  def getUserProfileByUserId(userId: UID): Option[UserProfile] = {
    connector.findAggregateByQuery((new BasicDBObject).append("userId", userId.uid), classOf[UserProfile])
  }

  def getUserProfileById(id: UID): Option[UserProfile] = {
    connector.getAggregateById(id, classOf[UserProfile])
  }

  def getUserProfiles(list: List[User]) = Map[User, UserProfile]()

  def onEntityChanged(userProfile: UserProfile) {
    connector.insertAggregate(userProfile)
  }
}
