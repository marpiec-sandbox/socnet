package pl.marpiec.socnet.redundant.technologies

import pl.marpiec.socnet.model.ProgrammerProfile
import pl.marpiec.socnet.model.programmerprofile.KnownTechnology
import pl.marpiec.util.StringFormattingUtil

/**
 * @author Marcin Pieciukiewicz
 */

class BestTechologies {

  var mostPopularTechnologiesMap = Map[String, TechnologySimpleRating]()
  var mostPopularTechnologies = List[TechnologySimpleRating]()
  var mostLikedTechnologiesMap = Map[String, TechnologySimpleRating]()
  var mostLikedTechnologies = List[TechnologySimpleRating]()

  def updateRankingLists(newProfile: ProgrammerProfile, oldProfileOption: Option[ProgrammerProfile]) {
    addFromNewProfile(newProfile.technologyKnowledge)
    if (oldProfileOption.isDefined && oldProfileOption.get.version < newProfile.version) {
      removeFromOldProfile(oldProfileOption.get.technologyKnowledge)
    }

    mostPopularTechnologies = mostPopularTechnologiesMap.values.toList.sorted.reverse
    mostLikedTechnologies = mostLikedTechnologiesMap.values.toList.sorted.reverse
  }

  private def addFromNewProfile(technologies: Map[String, KnownTechnology]) {
    for ((technologyName, knowledge) <- technologies) {
      val simpleMostPopularRating = mostPopularTechnologiesMap.getOrElse(technologyName, TechnologySimpleRating(technologyName))
      mostPopularTechnologiesMap += technologyName -> simpleMostPopularRating.addVote(knowledge.knowledgeLevel.value)

      if (knowledge.likeLevelOption.isDefined) {
        val simpleMostLikedRating = mostLikedTechnologiesMap.getOrElse(technologyName, TechnologySimpleRating(technologyName))
        mostLikedTechnologiesMap += technologyName -> simpleMostLikedRating.addVote(knowledge.likeLevelOption.get.value)
      }
    }
  }

  private def removeFromOldProfile(technologies: Map[String, KnownTechnology]) {
    for ((technologyName, knowledge) <- technologies) {
      val simpleMostPopularRating = mostPopularTechnologiesMap.getOrElse(technologyName, TechnologySimpleRating(technologyName))
      mostPopularTechnologiesMap += technologyName -> simpleMostPopularRating.removeVote(knowledge.knowledgeLevel.value)

      if (knowledge.likeLevelOption.isDefined) {
        val simpleMostLikedRating = mostLikedTechnologiesMap.getOrElse(technologyName, TechnologySimpleRating(technologyName))
        mostLikedTechnologiesMap += technologyName -> simpleMostLikedRating.removeVote(knowledge.likeLevelOption.get.value)
      }
    }
  }

  def getMostPopularTechnologiesMatching(query: String, count: Int):List[String] = {

    val lowerCaseQuery = StringFormattingUtil.toLowerCase(query)
    var suggestionList = List[String]()

    mostPopularTechnologies.foreach(simpleRating => {
      if(suggestionList.size < count && StringFormattingUtil.toLowerCase(simpleRating.technologyName).contains(lowerCaseQuery)) {
        suggestionList ::= simpleRating.technologyName
      }
    })
    suggestionList.sorted
  }
}
