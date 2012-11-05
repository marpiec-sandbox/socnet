package pl.marpiec.socnet.readdatabase.mock

import org.springframework.stereotype.Repository
import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.socnet.readdatabase.UserActionsInfoDatabase
import pl.marpiec.util.UID
import pl.marpiec.cqrs.{Aggregate, DataStore}
import pl.marpiec.socnet.model.UserActionsInfo

/**
 * @author Marcin Pieciukiewicz
 */

@Repository("userActionsInfoDatabase")
class UserActionsInfoDatabaseMockImpl @Autowired()(dataStore: DataStore)
  extends AbstractDatabase[UserActionsInfo](dataStore) with UserActionsInfoDatabase {

  val USER_ID_INDEX = "userId"

  startListeningToDataStore(dataStore, classOf[UserActionsInfo])

  addIndex(USER_ID_INDEX, (aggregate: Aggregate) => {
    val userActionsInfo = aggregate.asInstanceOf[UserActionsInfo]
    userActionsInfo.userId
  });

  def getUserActionsInfoByUserId(userId: UID) = getByIndex(USER_ID_INDEX, userId)

  def getUserActionsInfoById(id: UID) = getById(id)
}
