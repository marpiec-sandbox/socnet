package pl.marpiec.socnet.service.user

import org.springframework.stereotype.Service
import pl.marpiec.util.UID
import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.socnet.service.userprofile.UserProfileCommand
import pl.marpiec.socnet.service.usercontacts.UserContactsCommand
import pl.marpiec.cqrs.UidGenerator
import pl.marpiec.socnet.service.programmerprofile.ProgrammerProfileCommand

/**
 * @author Marcin Pieciukiewicz
 */

@Service("userRegistrationCommand")
class UserRegistrationCommandImpl extends UserRegistrationCommand {

  @Autowired
  var userProfileCommand: UserProfileCommand = _
  @Autowired
  var userContactsCommand: UserContactsCommand = _
  @Autowired
  var programmerProfileCommand: ProgrammerProfileCommand = _
  @Autowired
  var uidGenerator: UidGenerator = _
  @Autowired
  var userCommand: UserCommand = _

  def triggerUserRegistrationProcess(trigger: String): UID = {
    val userId = userCommand.triggerUserRegistration(trigger)

    userProfileCommand.createUserProfile(userId, userId, uidGenerator.nextUid)
    userContactsCommand.createUserContacts(userId, userId, uidGenerator.nextUid)
    programmerProfileCommand.createProgrammerProfile(userId, userId, uidGenerator.nextUid)

    userId
  }
}
