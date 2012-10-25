package pl.marpiec.socnet.readdatabase.nosql

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import pl.marpiec.socnet.readdatabase.ProgrammerProfileDatabase
import pl.marpiec.util.UID
import pl.marpiec.cqrs.{DataStoreListener, DataStore}
import pl.marpiec.socnet.mongodb.DatabaseConnectorImpl
import pl.marpiec.socnet.model.ProgrammerProfile
import com.mongodb.QueryBuilder
import pl.marpiec.socnet.redundant.technologies.BestTechologies

/**
 * @author Marcin Pieciukiewicz
 */

@Repository("programmerProfileDatabase")
class ProgrammerProfileDatabaseNoSqlImpl @Autowired()(dataStore: DataStore)
  extends DataStoreListener[ProgrammerProfile] with ProgrammerProfileDatabase {

  val connector = new DatabaseConnectorImpl("programmersProfiles")

  val bestTechnologies = new BestTechologies

  startListeningToDataStore(dataStore, classOf[ProgrammerProfile])

  def onEntityChanged(programmerProfile: ProgrammerProfile) = {
    val oldProgrammerProfileOption = getProgrammerProfileByUserId(programmerProfile.userId)
    connector.insertAggregate(programmerProfile)
    bestTechnologies.updateRankingLists(programmerProfile, oldProgrammerProfileOption)
  }

  def getProgrammerProfileByUserId(userId: UID) = {
    connector.findAggregateByQuery(QueryBuilder.start("userId").is(userId.uid).get(), classOf[ProgrammerProfile])
  }

  def getMostPopularTechnologies(count: Int) = bestTechnologies.mostPopularTechnologies.take(count)

  def getMostLikedTechnologies(count: Int) = bestTechnologies.mostLikedTechnologies.take(count)

  def getMostPopularTechnologiesMatching(query: String, count: Int):List[String] = {
    bestTechnologies.getMostPopularTechnologiesMatching(query, count)
  }
}
