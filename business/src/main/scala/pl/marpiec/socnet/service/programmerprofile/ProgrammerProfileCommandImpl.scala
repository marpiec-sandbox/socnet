package pl.marpiec.socnet.service.programmerprofile

import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.cqrs.EventStore
import pl.marpiec.util.UID

class ProgrammerProfileCommandImpl @Autowired()(val eventStore: EventStore) extends ProgrammerProfileCommand {

  def changeTechnologies(userId: UID, programmerProfileId: UID, programmerProfileVersion: Int, technologiesChanged: Map[String, Int], technologiesRemoved: List[String]) {

  }

  def createProgrammerProfile(userId: UID, programmerUserId: UID, newProgrammerProfileId: UID) {

  }

}
