package pl.marpiec.socnet.service.userroles

import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

trait UserRolesCommand {

  def createUserRoles(userId: UID, rolesForUserId: UID, newUserRolesId: UID)
  def addUserRole(userId: UID, id: UID, version: Int, role: String)
  def removeUserRole(userId: UID, id: UID, version: Int, role: String)

}
