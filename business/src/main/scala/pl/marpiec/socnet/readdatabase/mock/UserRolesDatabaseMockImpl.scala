package pl.marpiec.socnet.readdatabase.mock

import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.socnet.model.{UserRoles, UserProfile}
import pl.marpiec.socnet.readdatabase.{UserRolesDatabase, UserProfileDatabase}
import pl.marpiec.util.UID
import pl.marpiec.cqrs.{Aggregate, DataStore}
import org.springframework.stereotype.Repository

/**
 * @author Marcin Pieciukiewicz
 */

@Repository("userRolesDatabase")
class UserRolesDatabaseMockImpl @Autowired()(dataStore: DataStore)
  extends AbstractDatabase[UserRoles](dataStore) with UserRolesDatabase {

  val USER_ID_INDEX = "userId"

  startListeningToDataStore(dataStore, classOf[UserRoles])

  addIndex(USER_ID_INDEX, (aggregate: Aggregate) => {
    val userRoles = aggregate.asInstanceOf[UserRoles]
    userRoles.userId
  });


  override def getById(id: UID) = super.getById(id)

  def getRolesByUserId(userId: UID) = getByIndex(USER_ID_INDEX, userId)
}
