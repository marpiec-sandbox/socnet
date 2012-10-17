package pl.marpiec.socnet.readdatabase

import pl.marpiec.util.UID
import pl.marpiec.socnet.model.UserRoles

/**
 * @author Marcin Pieciukiewicz
 */

trait UserRolesDatabase {

  def getById(userId: UID): Option[UserRoles]
  def getRolesByUserId(userId: UID): Option[UserRoles]

}
