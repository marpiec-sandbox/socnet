package pl.marpiec.socnet.service.user

import org.springframework.stereotype.Service
import pl.marpiec.util.UID
import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.socnet.service.userprofile.UserProfileCommand
import pl.marpiec.socnet.service.usercontacts.UserContactsCommand
import pl.marpiec.cqrs.UidGenerator
import pl.marpiec.socnet.service.programmerprofile.ProgrammerProfileCommand
import pl.marpiec.socnet.service.userroles.UserRolesCommand
import pl.marpiec.socnet.service.useractionsinfo.UserActionsInfoCommand

/**
 * @author Marcin Pieciukiewicz
 */

@Service("userRegistrationCommand")
class UserRegistrationCommandImpl extends UserRegistrationCommand {

  @Autowired
  var userProfileCommand: UserProfileCommand = _
  @Autowired
  var userRolesCommand: UserRolesCommand = _
  @Autowired
  var userContactsCommand: UserContactsCommand = _
  @Autowired
  var programmerProfileCommand: ProgrammerProfileCommand = _
  @Autowired
  var userActionsInfoCommand: UserActionsInfoCommand = _
  @Autowired
  var uidGenerator: UidGenerator = _
  @Autowired
  var userCommand: UserCommand = _

  def triggerUserRegistrationProcess(trigger: String): UID = {
    val userId = userCommand.triggerUserRegistration(trigger)

    userRolesCommand.createUserRoles(userId, userId, uidGenerator.nextUid)
    userProfileCommand.createUserProfile(userId, userId, uidGenerator.nextUid)
    userContactsCommand.createUserContacts(userId, userId, uidGenerator.nextUid)
    programmerProfileCommand.createProgrammerProfile(userId, userId, uidGenerator.nextUid)
    userActionsInfoCommand.createUserActionsInfo(userId, userId, uidGenerator.nextUid)

    userId
  }
}
