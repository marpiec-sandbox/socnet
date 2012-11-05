package pl.marpiec.socnet.service.useractionsinfo

import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.socnet.service.usercontacts.{UserContactsCommandImpl, UserContactsCommand}
import pl.marpiec.socnet.service.contactinvitation.{ContactInvitationCommandImpl, ContactInvitationCommand}
import pl.marpiec.cqrs._
import pl.marpiec.socnet.readdatabase.mock.{UserActionsInfoDatabaseMockImpl, ContactInvitationDatabaseMockImpl, UserContactsDatabaseMockImpl}
import pl.marpiec.socnet.readdatabase.{UserActionsInfoDatabase, ContactInvitationDatabase, UserContactsDatabase}
import org.joda.time.LocalDateTime


/**
 * @author Marcin Pieciukiewicz
 */

@Test
class UserActionsInfoCommandTest {

  def testSimpleInfos() {

    val eventStore: EventStore = new EventStoreMockImpl
    val aggregateCache: AggregateCache = new AggregateCacheSimpleImpl

    val dataStore: DataStore = new DataStoreImpl(eventStore, aggregateCache)
    val userActionsInfoDatabase: UserActionsInfoDatabase = new UserActionsInfoDatabaseMockImpl(dataStore)
    val userActionsInfoCommand: UserActionsInfoCommand = new UserActionsInfoCommandImpl(eventStore)

    val uidGenerator: UidGenerator = new UidGeneratorMockImpl

    
    val userId = uidGenerator.nextUid
    val userActionsInfoId = uidGenerator.nextUid

    userActionsInfoCommand.createUserActionsInfo(userId, userId, userActionsInfoId)

    val userActionsInfoOption = userActionsInfoDatabase.getUserActionsInfoByUserId(userId)
    
    assertTrue(userActionsInfoOption.isDefined)
    
    var userActionsInfo = userActionsInfoOption.get

    assertTrue(userActionsInfo.contactInvitationsReadTimeOption.isEmpty)
    
    val readTime = new LocalDateTime(2012, 11, 14, 12, 30, 23)

    userActionsInfoCommand.changeContactInvitationsReadTime(userId, userActionsInfoId, readTime)

    userActionsInfo = userActionsInfoDatabase.getUserActionsInfoByUserId(userId).get

    assertEquals(userActionsInfo.contactInvitationsReadTimeOption.get, new LocalDateTime(2012, 11, 14, 12, 30, 23))
    
  }
}
