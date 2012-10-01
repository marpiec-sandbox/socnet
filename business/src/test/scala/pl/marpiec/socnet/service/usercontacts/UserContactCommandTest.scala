package pl.marpiec.socnet.service.usercontacts

import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.cqrs._
import pl.marpiec.socnet.model.UserContacts
import pl.marpiec.socnet.readdatabase.mock.UserContactsDatabaseMockImpl

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

    //Create users contacts

    userContactsCommand.createUserContacts(userId, userId, userContactsId)
    userContactsCommand.createUserContacts(firstContactUserId, firstContactUserId, firstContactContactsId)

    var userContacts = dataStore.getEntity(classOf[UserContacts], userContactsId).asInstanceOf[UserContacts]
    var firstContactContacts = dataStore.getEntity(classOf[UserContacts], firstContactContactsId).asInstanceOf[UserContacts]

    assertNotNull(userContacts)
    assertNotNull(firstContactContacts)

    val firstInvitationId = uidGenerator.nextUid

    //send invitation

    userContactsCommand.sendInvitation(userId, userContacts.id, firstContactUserId, "Hello", firstInvitationId)

    userContacts = dataStore.getEntity(classOf[UserContacts], userContactsId).asInstanceOf[UserContacts]
    firstContactContacts = dataStore.getEntity(classOf[UserContacts], firstContactContactsId).asInstanceOf[UserContacts]

    assertEquals(userContacts.invitationsSent.size, 1)
    assertEquals(userContacts.invitationsReceived.size, 0)
    assertEquals(userContacts.contacts.size, 0)

    assertEquals(firstContactContacts.invitationsSent.size, 0)
    assertEquals(firstContactContacts.invitationsReceived.size, 1)
    assertEquals(firstContactContacts.contacts.size, 0)

    assertEquals(userContacts.invitationsSent.head.possibleContactUserId, firstContactUserId)
    assertEquals(firstContactContacts.invitationsReceived.head.possibleContactUserId, userId)

    // accept invitation
    userContactsCommand.acceptInvitation(firstContactUserId, firstContactContacts.id, userId, firstInvitationId)

    userContacts = dataStore.getEntity(classOf[UserContacts], userContactsId).asInstanceOf[UserContacts]
    firstContactContacts = dataStore.getEntity(classOf[UserContacts], firstContactContactsId).asInstanceOf[UserContacts]

    assertEquals(userContacts.invitationsSent.size, 1)
    assertEquals(userContacts.invitationsReceived.size, 0)
    assertEquals(userContacts.contacts.size, 1)

    assertEquals(firstContactContacts.invitationsSent.size, 0)
    assertEquals(firstContactContacts.invitationsReceived.size, 1)
    assertEquals(firstContactContacts.contacts.size, 1)

    assertEquals(userContacts.invitationsSent.head.accepted, true)
    assertEquals(userContacts.contacts.head.contactUserId, firstContactUserId)

    assertEquals(firstContactContacts.invitationsReceived.head.accepted, true)
    assertEquals(firstContactContacts.contacts.head.contactUserId, userId)

  }
}
