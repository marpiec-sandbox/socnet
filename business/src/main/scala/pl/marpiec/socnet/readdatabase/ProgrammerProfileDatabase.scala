package pl.marpiec.socnet.readdatabase

import pl.marpiec.util.UID
import pl.marpiec.socnet.model.ProgrammerProfile
import pl.marpiec.socnet.redundant.technologies.TechnologySimpleRating

trait ProgrammerProfileDatabase {
  def getProgrammerProfileByUserId(userId: UID):Option[ProgrammerProfile]
  def getMostPopularTechnologies(count:Int):List[TechnologySimpleRating]
  def getMostLikedTechnologies(count:Int):List[TechnologySimpleRating]
  def getMostPopularTechnologiesMatching(query:String, count:Int):List[String]
}
