package pl.marpiec.socnet.service.programmerprofile.event

import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.socnet.model.ProgrammerProfile
import pl.marpiec.socnet.model.programmerprofile.KnownTechnology

/**
 * @author Marcin Pieciukiewicz
 */

class ChangeKnownTechnologiesEvent(val technologiesChanged: Array[KnownTechnology], val technologiesRemoved: List[String]) extends Event {

  def entityClass = classOf[ProgrammerProfile]

  def applyEvent(aggregate: Aggregate) {
    val programmerProfile = aggregate.asInstanceOf[ProgrammerProfile]

    programmerProfile.technologyKnowledge = programmerProfile.technologyKnowledge.filterKeys(technology => {
      !technologiesRemoved.contains(technology) && !technologiesChanged.contains(technology)
    })

    technologiesChanged.foreach(technology => {
      programmerProfile.technologyKnowledge += technology.name -> technology
    })

  }
}
