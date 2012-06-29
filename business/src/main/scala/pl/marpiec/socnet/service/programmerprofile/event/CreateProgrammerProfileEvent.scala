package pl.marpiec.socnet.service.programmerprofile.event

import pl.marpiec.util.UID
import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.socnet.model.ProgrammerProfile

/**
 * @author Marcin Pieciukiewicz
 */

class CreateProgrammerProfileEvent(val userId: UID) extends Event {

  def entityClass = classOf[ProgrammerProfile]

  def applyEvent(aggregate: Aggregate) {
    val programmerProfile = aggregate.asInstanceOf[ProgrammerProfile]
    programmerProfile.userId = userId
  }
}
