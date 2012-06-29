package pl.marpiec.socnet.readdatabase

import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.cqrs.{Aggregate, DataStore}
import pl.marpiec.socnet.model.ProgrammerProfile
import pl.marpiec.util.UID

class ProgrammerProfileDatabaseMockImpl @Autowired()(dataStore: DataStore)
  extends AbstractDatabase[ProgrammerProfile](dataStore) with ProgrammerProfileDatabase {

  val USER_ID_INDEX = "userId"

  startListeningToDataStore(dataStore, classOf[ProgrammerProfile])

  addIndex(USER_ID_INDEX, (aggregate: Aggregate) => {
    val programmerProfile = aggregate.asInstanceOf[ProgrammerProfile]
    programmerProfile.userId
  });

  def getProgrammerProfileByUserId(userId: UID) = getByIndex(USER_ID_INDEX, userId)

}
