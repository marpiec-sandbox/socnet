package pl.marpiec.socnet.service.programmerprofile

import event.{ChangeKnownTechnologiesEvent, CreateProgrammerProfileEvent}
import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.util.UID
import pl.marpiec.cqrs.{EventRow, EventStore}
import org.springframework.stereotype.Service
import pl.marpiec.socnet.model.programmerprofile.KnownTechnology

@Service("programmerProfileCommand")
class ProgrammerProfileCommandImpl @Autowired()(val eventStore: EventStore) extends ProgrammerProfileCommand {

  def createProgrammerProfile(userId: UID, programmerUserId: UID, newProgrammerProfileId: UID) {
    val createProgrammerProfile = new CreateProgrammerProfileEvent(programmerUserId)
    eventStore.addEventForNewAggregate(newProgrammerProfileId, new EventRow(userId, newProgrammerProfileId, 0, createProgrammerProfile))
  }

  def changeTechnologies(userId: UID, programmerProfileId: UID, programmerProfileVersion: Int, technologiesChanged: List[KnownTechnology], technologiesRemoved: List[String]) {
    
    val technologiesChangedArray:Array[KnownTechnology] = new Array[KnownTechnology](technologiesChanged.size)

    var counter = 0
    for (knownTechnology <- technologiesChanged) {
      technologiesChangedArray(counter) = (knownTechnology)
      counter = counter + 1
    }

    eventStore.addEvent(new EventRow(userId, programmerProfileId, programmerProfileVersion,
      new ChangeKnownTechnologiesEvent(technologiesChangedArray, technologiesRemoved.toArray)))
  }


}
