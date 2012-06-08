package socnet.service.conversation


import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.socnet.service.userprofile.{UserProfileCommandImpl, UserProfileCommand}
import pl.marpiec.util.UID
import pl.marpiec.socnet.model.UserProfile
import pl.marpiec.cqrs._
import socnet.model.Conversation

/**
 * @author Marcin Pieciukiewicz
 */

@Test
class ConversationCommandTest {
  def testSimpleConversation() {
    val eventStore: EventStore = new EventStoreMockImpl
    val entityCache: AggregateCache = new AggregateCacheSimpleImpl
    val dataStore: DataStore = new DataStoreImpl(eventStore, entityCache)
    val conversationCommand: ConversationCommand = new ConversationCommandImpl(eventStore)
    val uidGenerator: UidGenerator = new UidGeneratorMockImpl

    val conversationId = uidGenerator.nextUid
    val participantAUserId = uidGenerator.nextUid
    val participantBUserId = uidGenerator.nextUid
    val participantCUserId = uidGenerator.nextUid


    val participantsIds = participantCUserId::participantBUserId::participantAUserId::Nil

    conversationCommand.createConversation(participantAUserId, "Test conversation", participantsIds, conversationId)

    var conversation = dataStore.getEntity(classOf[Conversation], conversationId).asInstanceOf[Conversation]

    assertNotNull(conversation)
    assertEquals(conversation.title, "Test conversation")
    assertEquals(conversation.creatorUserId, participantAUserId)

    val firstMessageId = uidGenerator.nextUid

    conversationCommand.createMessage(participantAUserId, conversationId, conversation.version, "Hello everybody", firstMessageId)

    conversation = dataStore.getEntity(classOf[Conversation], conversationId).asInstanceOf[Conversation]

    assertEquals(conversation.messages.size, 1)
    val firstMessage = conversation.messages.head
    assertEquals(firstMessage.messageText, "Hello everybody")
    assertEquals(firstMessage.authorUserId, participantAUserId)
    assertNotNull(firstMessage.sentTime)
    assertEquals(firstMessage.id, firstMessageId)

    val secondMessageId = uidGenerator.nextUid

    conversationCommand.createMessage(participantBUserId, conversationId, conversation.version, "Hello first user", secondMessageId)

    conversation = dataStore.getEntity(classOf[Conversation], conversationId).asInstanceOf[Conversation]

    assertEquals(conversation.messages.size, 2)
    val secondMessage = conversation.messages.head
    assertEquals(secondMessage.messageText, "Hello first user")
    assertEquals(secondMessage.authorUserId, participantBUserId)
    assertNotNull(secondMessage.sentTime)
    assertEquals(secondMessage.id, secondMessageId)

  }
}
