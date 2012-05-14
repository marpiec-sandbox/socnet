package socnet.service.usercontacts

import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.cqrs._
import socnet.model.UserContacts
import socnet.readdatabase.UserContactsDatabaseMockImpl

/**
 * @author Marcin Pieciukiewicz
 */

@Test
class UserContactCommandTest {

  def testAddingContact() {
    val eventStore: EventStore = new EventStoreMockImpl
    val aggregateCache: AggregateCache = new AggregateCacheSimpleImpl

    val dataStore: DataStore = new DataStoreImpl(eventStore, aggregateCache)
    val userContactsDatabase = new UserContactsDatabaseMockImpl(dataStore)
    val userContactsCommand = new UserContactsCommandImpl(eventStore, userContactsDatabase)

    val uidGenerator: UidGenerator = new UidGeneratorMockImpl

    val userId = uidGenerator.nextUid
    val userContactsId = uidGenerator.nextUid
    val firstContactUserId = uidGenerator.nextUid
    val firstContactContactsId = uidGenerator.nextUid

    userContactsCommand.createUserContacts(userId, userId, userContactsId)
    userContactsCommand.createUserContacts(firstContactUserId, firstContactUserId, firstContactContactsId)

    var userContacts = dataStore.getEntity(classOf[UserContacts], userContactsId).asInstanceOf[UserContacts]
    var firstContactContacts = dataStore.getEntity(classOf[UserContacts], firstContactContactsId).asInstanceOf[UserContacts]

    assertNotNull(userContacts)
    assertNotNull(firstContactContacts)

    val firstInvitationId = uidGenerator.nextUid

    userContactsCommand.sendInvitation(userId, userContacts.id, 0, firstContactUserId, "Hello", firstInvitationId)

    userContacts = dataStore.getEntity(classOf[UserContacts], userContactsId).asInstanceOf[UserContacts]

    assertEquals(userContacts.invitationsSent.size, 1)
    assertEquals(userContacts.invitationsReceived.size, 0)

    firstContactContacts = dataStore.getEntity(classOf[UserContacts], firstContactContactsId).asInstanceOf[UserContacts]

    assertEquals(firstContactContacts.invitationsSent.size, 0)
    assertEquals(firstContactContacts.invitationsReceived.size, 1)
  }
}
