package pl.marpiec.socnet.service.programmerprofile

import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

trait ProgrammerProfileCommand {

  def changeTechnologies(userId: UID, programmerProfileId: UID, programmerProfileVersion: Int, technologiesChanged: Map[String, Int], technologiesRemoved: List[String])
  def createProgrammerProfile(userId: UID, programmerUserId: UID, newProgrammerProfileId: UID)

}
