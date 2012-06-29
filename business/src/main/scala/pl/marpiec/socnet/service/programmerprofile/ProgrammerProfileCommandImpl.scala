package pl.marpiec.socnet.service.programmerprofile

import event.{ChangeKnownTechnologiesEvent, CreateProgrammerProfileEvent}
import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.util.UID
import pl.marpiec.cqrs.{EventRow, EventStore}
import org.springframework.stereotype.Service

@Service("programmerProfileCommand")
class ProgrammerProfileCommandImpl @Autowired()(val eventStore: EventStore) extends ProgrammerProfileCommand {

  def createProgrammerProfile(userId: UID, programmerUserId: UID, newProgrammerProfileId: UID) {
    val createProgrammerProfile = new CreateProgrammerProfileEvent(programmerUserId)
    eventStore.addEventForNewAggregate(newProgrammerProfileId, new EventRow(userId, newProgrammerProfileId, 0, createProgrammerProfile))
  }

  def changeTechnologies(userId: UID, programmerProfileId: UID, programmerProfileVersion: Int, technologiesChanged: Map[String, Int], technologiesRemoved: List[String]) {
    
    val technologiesChangedArray:Array[(String,  String)] = new Array[(String,  String)](technologiesChanged.size)

    var counter = 0
    for ((key, value) <- technologiesChanged) {
      technologiesChangedArray(counter) = (key, value.toString)
      counter = counter + 1
    }

    eventStore.addEvent(new EventRow(userId, programmerProfileId, programmerProfileVersion,
      new ChangeKnownTechnologiesEvent(technologiesChangedArray, technologiesRemoved.toArray)))
  }


}
