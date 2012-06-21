package pl.marpiec.socnet.service.user

import pl.marpiec.util.UID

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

trait UserCommand {

  // @throws(classOf[EntryAlreadyExistsException])
  def createRegisterUserTrigger(firstName: String, lastName: String, email: String, password: String): String

  def triggerUserRegistration(trigger: String): UID

  def changeUserEmail(userId: UID, aggregateUserId: UID, version: Int, email: String)

  def changeUserSummary(userId: UID, aggregateUserId: UID, version: Int, firstName:String, lastName:String, summary: String)

  def createChangeForgottenPasswordTrigger(email: String)

  def triggerChangePassword(userId: UID, aggregateUserId: UID, version: Int, newPassword: String, trigger: String)
}
