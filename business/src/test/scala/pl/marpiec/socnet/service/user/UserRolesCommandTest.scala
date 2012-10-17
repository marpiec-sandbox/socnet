package pl.marpiec.socnet.service.user

import org.testng.Assert._
import org.testng.annotations.Test
import pl.marpiec.cqrs._
import pl.marpiec.socnet.model.UserRoles
import pl.marpiec.socnet.service.userroles.{UserRolesCommand, UserRolesCommandImpl}
import pl.marpiec.socnet.readdatabase.UserRolesDatabase
import pl.marpiec.socnet.readdatabase.mock.UserRolesDatabaseMockImpl

/**
 * @author Marcin Pieciukiewicz
 */

@Test
class UserRolesCommandTest {


  def testUserRoleChanging() {
    val eventStore: EventStore = new EventStoreMockImpl
    val entityCache: AggregateCache = new AggregateCacheSimpleImpl
    val dataStore: DataStore = new DataStoreImpl(eventStore, entityCache)
    val userRolesDatabase: UserRolesDatabase = new UserRolesDatabaseMockImpl(dataStore)
    val uidGenerator: UidGenerator = new UidGeneratorMockImpl

    val userRolesCommand: UserRolesCommand = new UserRolesCommandImpl(eventStore)

    val userId = uidGenerator.nextUid
    val userRolesId = uidGenerator.nextUid

    val USER_ROLE = "user"
    val TRUSTED_USER_ROLE = "trustedUser"

    userRolesCommand.createUserRoles(userId, userId, userRolesId)

    val userRolesOption: Option[UserRoles] = userRolesDatabase.getRolesByUserId(userId)

    assertTrue(userRolesOption.isDefined)
    var userRoles = userRolesOption.get
    assertEquals(userRoles.roles.size, 1)

    assertTrue(userRoles.roles.contains(USER_ROLE))

    userRolesCommand.addUserRole(userId, userRoles.id, userRoles.version, TRUSTED_USER_ROLE)
    userRoles = userRolesDatabase.getById(userRolesId).get

    assertEquals(userRoles.roles.size, 2)
    assertTrue(userRoles.roles.contains(USER_ROLE))
    assertTrue(userRoles.roles.contains(TRUSTED_USER_ROLE))

    userRolesCommand.removeUserRole(userId, userRoles.id, userRoles.version, TRUSTED_USER_ROLE)
    userRoles = userRolesDatabase.getRolesByUserId(userId).get

    assertEquals(userRoles.roles.size, 1)
    assertTrue(userRoles.roles.contains(USER_ROLE))

  }


}
