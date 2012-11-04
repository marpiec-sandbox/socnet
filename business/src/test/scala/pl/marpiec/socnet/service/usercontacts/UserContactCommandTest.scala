package pl.marpiec.socnet.service.usercontacts

import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.cqrs._
import pl.marpiec.socnet.model.UserContacts
import pl.marpiec.socnet.readdatabase.mock.{ContactInvitationDatabaseMockImpl, UserContactsDatabaseMockImpl}
import pl.marpiec.socnet.service.contactinvitation.{ContactInvitationCommand, ContactInvitationCommandImpl}
import pl.marpiec.socnet.readdatabase.{UserContactsDatabase, ContactInvitationDatabase}

/**
 * @author Marcin Pieciukiewicz
 */

@Test
class UserContactCommandTest {

  def testAddingContact() {
    val eventStore: EventStore = new EventStoreMockImpl
    val aggregateCache: AggregateCache = new AggregateCacheSimpleImpl

    val dataStore: DataStore = new DataStoreImpl(eventStore, aggregateCache)
    val userContactsDatabase:UserContactsDatabase = new UserContactsDatabaseMockImpl(dataStore)
    val userContactsCommand:UserContactsCommand = new UserContactsCommandImpl(eventStore, userContactsDatabase)

    val contactInvitationCommand:ContactInvitationCommand = new ContactInvitationCommandImpl(eventStore, userContactsCommand)
    val contactInvitationDatabase: ContactInvitationDatabase = new ContactInvitationDatabaseMockImpl(dataStore)

    val uidGenerator: UidGenerator = new UidGeneratorMockImpl

    val userId = uidGenerator.nextUid
    val userContactsId = uidGenerator.nextUid
    val firstContactUserId = uidGenerator.nextUid
    val firstContactContactsId = uidGenerator.nextUid

    //Create users contacts

    userContactsCommand.createUserContacts(userId, userId, userContactsId)
    userContactsCommand.createUserContacts(firstContactUserId, firstContactUserId, firstContactContactsId)

    val userContactsOption = userContactsDatabase.getUserContactsByUserId(userId)
    val firstContactContactsOption = userContactsDatabase.getUserContactsByUserId(firstContactUserId)

    assertTrue(userContactsOption.isDefined)
    assertTrue(firstContactContactsOption.isDefined)

    val firstInvitationId = uidGenerator.nextUid

    //send invitation

    contactInvitationCommand.sendInvitation(userId, firstContactUserId, "Hello", firstInvitationId)

    val userSentInvitations = contactInvitationDatabase.getSentInvitations(userId)
    val userReceivedInvitations = contactInvitationDatabase.getReceivedInvitations(userId)
    val firstContactSentInvitations = contactInvitationDatabase.getSentInvitations(firstContactUserId)
    val firstContactReceivedInvitations = contactInvitationDatabase.getReceivedInvitations(firstContactUserId)

    assertEquals(userSentInvitations.size, 1)
    assertTrue(userReceivedInvitations.isEmpty)
    assertTrue(firstContactSentInvitations.isEmpty)
    assertEquals(firstContactReceivedInvitations.size, 1)

    assertEquals(userSentInvitations.head.id, firstContactReceivedInvitations.head.id)
    val firstInvitation = userSentInvitations.head
    assertTrue(firstInvitation.isSent)
    assertFalse(firstInvitation.isCanceled)
    assertFalse(firstInvitation.isAccepted)
    assertFalse(firstInvitation.isDeclined)

    // accept invitation
    contactInvitationCommand.acceptInvitation(firstContactUserId, firstInvitation.id, firstInvitation.version, userId, firstContactUserId)

    var userContacts = userContactsDatabase.getUserContactsByUserId(userId).get
    var firstContactContacts = userContactsDatabase.getUserContactsByUserId(firstContactUserId).get

    assertEquals(userContacts.contactsIds.size, 1)
    assertEquals(firstContactContacts.contactsIds.size, 1)

    assertEquals(userContacts.contactsIds.head, firstContactUserId)
    assertEquals(firstContactContacts.contactsIds.head, userId)


    //remove contact
    userContactsCommand.removeContact(userId, userId, firstContactUserId)

    userContacts = userContactsDatabase.getUserContactsByUserId(userId).get
    firstContactContacts = userContactsDatabase.getUserContactsByUserId(firstContactUserId).get

    assertTrue(userContacts.contactsIds.isEmpty)
    assertTrue(firstContactContacts.contactsIds.isEmpty)

  }
}
