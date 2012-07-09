package pl.marpiec.socnet.service.programmerprofile

import pl.marpiec.util.UID
import pl.marpiec.socnet.model.programmerprofile.KnownTechnology

/**
 * @author Marcin Pieciukiewicz
 */

trait ProgrammerProfileCommand {

  def changeTechnologies(userId: UID, programmerProfileId: UID, programmerProfileVersion: Int, technologiesChanged: List[KnownTechnology], technologiesRemoved: List[String])
  def createProgrammerProfile(userId: UID, programmerUserId: UID, newProgrammerProfileId: UID)

}
