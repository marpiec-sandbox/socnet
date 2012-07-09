package pl.marpiec.socnet.model.programmerprofile

import pl.marpiec.socnet.constant.{TechnologyCurrentUsage, TechnologyLikeLevel, TechnologyKnowledgeLevel}


/**
 * @author Marcin Pieciukiewicz
 */

class KnownTechnology(val name: String, var knowledgeLevel: TechnologyKnowledgeLevel, var likeLevelOption: Option[TechnologyLikeLevel],
                      var currentUsageOption: Option[TechnologyCurrentUsage]) {

}

object KnownTechnology {
  def apply(name: String, knowledgeLevel: TechnologyKnowledgeLevel): KnownTechnology = {
    new KnownTechnology(name, knowledgeLevel, None, None)
  }
}
