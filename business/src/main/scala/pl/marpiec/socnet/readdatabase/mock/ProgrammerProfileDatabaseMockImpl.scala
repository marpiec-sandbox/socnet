package pl.marpiec.socnet.readdatabase.mock

import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.cqrs.{Aggregate, DataStore}
import pl.marpiec.socnet.model.ProgrammerProfile
import pl.marpiec.util.UID
import pl.marpiec.socnet.readdatabase.ProgrammerProfileDatabase
import pl.marpiec.socnet.redundant.technologies.BestTechologies

class ProgrammerProfileDatabaseMockImpl @Autowired()(dataStore: DataStore)
  extends AbstractDatabase[ProgrammerProfile](dataStore) with ProgrammerProfileDatabase {

  val USER_ID_INDEX = "userId"

  val bestTechnologies = new BestTechologies

  startListeningToDataStore(dataStore, classOf[ProgrammerProfile])

  addIndex(USER_ID_INDEX, (aggregate: Aggregate) => {
    val programmerProfile = aggregate.asInstanceOf[ProgrammerProfile]
    programmerProfile.userId
  });

  override def onEntityChanged(programmerProfile: ProgrammerProfile) {
    val oldProgrammerProfileOption = getById(programmerProfile.id)
    super.onEntityChanged(programmerProfile)

    bestTechnologies.updateRankingLists(programmerProfile, oldProgrammerProfileOption)
  }

  def getProgrammerProfileByUserId(userId: UID): Option[ProgrammerProfile] = getByIndex(USER_ID_INDEX, userId)

  def getMostPopularTechnologies(count: Int) = bestTechnologies.mostPopularTechnologies.take(count)

  def getMostLikedTechnologies(count: Int) = bestTechnologies.mostPopularTechnologies.take(count)

  def getMostPopularTechnologiesMatching(query: String, count: Int):List[String] = {
    bestTechnologies.getMostPopularTechnologiesMatching(query, count)
  }
}
