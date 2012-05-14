package socnet.service.usercontacts

import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.cqrs._
import socnet.model.UserContacts

/**
 * @author Marcin Pieciukiewicz
 */

@Test
class UserContactCommandTest {

  def testAddingContact() {
    val eventStore: EventStore = new EventStoreMockImpl
    val aggregateCache: AggregateCache = new AggregateCacheSimpleImpl

    val dataStore: DataStore = new DataStoreImpl(eventStore, aggregateCache)
    val userContactsCommand = new UserContactsCommandImpl(eventStore)

    val uidGenerator: UidGenerator = new UidGeneratorMockImpl

    val userId = uidGenerator.nextUid
    val userContactsId = uidGenerator.nextUid
    val firstContactUserId = uidGenerator.nextUid
    //val firstContactContactsId = uidGenerator.nextUid

    userContactsCommand.createUserContacts(userId, userId, userContactsId)
    //userContactsCommand.createUserContacts(firstContactUserId, firstContactUserId, firstContactContactsId)

    var userContacts = dataStore.getEntity(classOf[UserContacts], userContactsId).asInstanceOf[UserContacts]

    val firstInvitationId = uidGenerator.nextUid

    userContactsCommand.sendInvitation(userId, userContacts.id, userContacts.version, firstContactUserId, "Hello", firstInvitationId)

    userContacts = dataStore.getEntity(classOf[UserContacts], userContactsId).asInstanceOf[UserContacts]

    assertEquals(userContacts.invitations.size, 1)

  }
}
