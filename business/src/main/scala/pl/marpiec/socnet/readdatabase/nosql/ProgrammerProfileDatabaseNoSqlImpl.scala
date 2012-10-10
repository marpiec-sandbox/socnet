package pl.marpiec.socnet.readdatabase.nosql

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import pl.marpiec.socnet.readdatabase.ProgrammerProfileDatabase
import pl.marpiec.util.UID
import pl.marpiec.cqrs.{DataStoreListener, DataStore}
import pl.marpiec.socnet.mongodb.DatabaseConnectorImpl
import pl.marpiec.socnet.model.ProgrammerProfile
import com.mongodb.QueryBuilder

/**
 * @author Marcin Pieciukiewicz
 */

@Repository("programmerProfileDatabase")
class ProgrammerProfileDatabaseNoSqlImpl @Autowired()(dataStore: DataStore)
  extends DataStoreListener[ProgrammerProfile] with ProgrammerProfileDatabase {

  val connector = new DatabaseConnectorImpl("programmersProfiles")

  startListeningToDataStore(dataStore, classOf[ProgrammerProfile])

  def onEntityChanged(programmerProfile: ProgrammerProfile) = {
    connector.insertAggregate(programmerProfile)
  }

  def getProgrammerProfileByUserId(userId: UID) = {
    connector.findAggregateByQuery(QueryBuilder.start("userId").is(userId.uid).get(), classOf[ProgrammerProfile])
  }
}
