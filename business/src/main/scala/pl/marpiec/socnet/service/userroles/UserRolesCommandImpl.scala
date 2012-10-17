package pl.marpiec.socnet.service.userroles

import event.{CreateUserRolesEvent, AddUserRoleEvent, RemoveUserRoleEvent}
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.util.UID
import pl.marpiec.cqrs.{EventRow, EventStore}
import pl.marpiec.socnet.constant.SocnetRoles

/**
 * @author Marcin Pieciukiewicz
 */

@Service("userRolesCommand")
class UserRolesCommandImpl @Autowired()(val eventStore: EventStore) extends UserRolesCommand {

  def createUserRoles(userId: UID, rolesForUserId: UID, newUserRolesId: UID) {
    val createUserRoles = new CreateUserRolesEvent(rolesForUserId, SocnetRoles.DEFAULT_ROLES)
    eventStore.addEventForNewAggregate(newUserRolesId, new EventRow(userId, newUserRolesId, 0, createUserRoles))
  }

  def addUserRole(userId: UID, id: UID, version: Int, role: String) {
    eventStore.addEvent(new EventRow(userId, id, version, new AddUserRoleEvent(role)))
  }

  def removeUserRole(userId: UID, id: UID, version: Int, role: String) {
    eventStore.addEvent(new EventRow(userId, id, version, new RemoveUserRoleEvent(role)))
  }
}
