package pl.marpiec.socnet.service.programmerprofile.event

import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.socnet.model.ProgrammerProfile

/**
 * @author Marcin Pieciukiewicz
 */

class ChangeKnownTechnologiesEvent(technologiesChanged: Map[String, Int], technologiesRemoved: List[String]) extends Event {

  def entityClass = classOf[ProgrammerProfile]

  def applyEvent(aggregate: Aggregate) {
    val programmerProfile = aggregate.asInstanceOf[ProgrammerProfile]

    programmerProfile.technologyKnowledge = programmerProfile.technologyKnowledge.filterKeys(technology => {
      !technologiesRemoved.contains(technology) && !technologiesChanged.contains(technology)
    })

    programmerProfile.technologyKnowledge ++= technologiesChanged

  }
}
