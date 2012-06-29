package pl.marpiec.socnet.web.page.usertechnologies.model

import pl.marpiec.socnet.web.wicket.SecureFormModel
import pl.marpiec.socnet.constant.TechnologyKnowledgeLevel

/**
 * @author Marcin Pieciukiewicz
 */

class AddTechnologyFormModel extends SecureFormModel {
  var technologyName: String = ""
  var knowledgeLevel: TechnologyKnowledgeLevel = _
}
