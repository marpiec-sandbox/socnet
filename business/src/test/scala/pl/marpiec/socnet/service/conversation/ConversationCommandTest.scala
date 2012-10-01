package pl.marpiec.socnet.service.conversation

import org.testng.annotations.Test
import pl.marpiec.cqrs._
import pl.marpiec.socnet.model.Conversation
import org.testng.Assert._
import pl.marpiec.socnet.readdatabase.mock.ConversationInfoDatabaseMockImpl
import pl.marpiec.socnet.readdatabase.ConversationInfoDatabase

/**
 * @author Marcin Pieciukiewicz
 */

@Test
class ConversationCommandTest {
  def testSimpleConversation() {
    val eventStore: EventStore = new EventStoreMockImpl
    val entityCache: AggregateCache = new AggregateCacheSimpleImpl
    val dataStore: DataStore = new DataStoreImpl(eventStore, entityCache)
    val uidGenerator: UidGenerator = new UidGeneratorMockImpl
    val conversationCommand: ConversationCommand = new ConversationCommandImpl(eventStore, uidGenerator)
    val conversationInfoDatabase: ConversationInfoDatabase = new ConversationInfoDatabaseMockImpl(dataStore);

    val conversationId = uidGenerator.nextUid
    val participantAUserId = uidGenerator.nextUid
    val participantBUserId = uidGenerator.nextUid
    val participantCUserId = uidGenerator.nextUid


    val participantsIds = participantCUserId :: participantBUserId :: participantAUserId :: Nil
    val firstMessageId = uidGenerator.nextUid

    // Conversation created

    conversationCommand.createConversation(participantAUserId, "Test conversation", participantsIds, conversationId,
      "Hello everybody", firstMessageId)

    var conversation = dataStore.getEntity(classOf[Conversation], conversationId).asInstanceOf[Conversation]

    assertNotNull(conversation)
    assertEquals(conversation.title, "Test conversation")
    assertEquals(conversation.creatorUserId, participantAUserId)
    assertEquals(conversation.participantsUserIds.length, 3)
    assertEquals(conversation.messages.size, 1)

    var conversationInfoForParticipantAOption = conversationInfoDatabase.getConversationInfo(participantAUserId, conversationId)
    assertTrue(conversationInfoForParticipantAOption.isDefined)

    // First User reads conversation

    var conversationInfoForParticipantA = conversationInfoForParticipantAOption.get

    conversationCommand.userHasReadConversation(participantAUserId, conversationInfoForParticipantA.id, conversationInfoForParticipantA.version)

    val secondMessageId = uidGenerator.nextUid

    // Sending second message by creator

    conversationCommand.createMessage(participantAUserId, conversationId, conversation.version, "Hello everybody", secondMessageId)

    conversation = dataStore.getEntity(classOf[Conversation], conversationId).asInstanceOf[Conversation]

    assertEquals(conversation.messages.size, 2)
    val firstMessage = conversation.messages.head
    assertEquals(firstMessage.messageText, "Hello everybody")
    assertEquals(firstMessage.authorUserId, participantAUserId)
    assertNotNull(firstMessage.sentTime)
    assertEquals(firstMessage.id, secondMessageId)

    val thirdMessageId = uidGenerator.nextUid

    // sending message by other participant

    conversationCommand.createMessage(participantBUserId, conversationId, conversation.version, "Hello first user", thirdMessageId)

    conversation = dataStore.getEntity(classOf[Conversation], conversationId).asInstanceOf[Conversation]

    assertEquals(conversation.messages.size, 3)
    val secondMessage = conversation.messages.head
    assertEquals(secondMessage.messageText, "Hello first user")
    assertEquals(secondMessage.authorUserId, participantBUserId)
    assertNotNull(secondMessage.sentTime)
    assertEquals(secondMessage.id, thirdMessageId)

    // Adding fourth participant

    val participantDUserId = uidGenerator.nextUid
    conversationCommand.addParticipant(participantBUserId, conversationId, conversation.version, "Hi Marcin, join our discussion", participantDUserId)

    conversation = dataStore.getEntity(classOf[Conversation], conversationId).asInstanceOf[Conversation]
    assertEquals(conversation.participantsUserIds.length, 4)

    // Second participant removes conversation from his conversations

    var participantBConversationInfo = conversationInfoDatabase.getConversationInfo(participantBUserId, conversationId).get
    conversationCommand.removeConversation(participantBUserId, participantBConversationInfo.id, participantBConversationInfo.version)
    conversation = dataStore.getEntity(classOf[Conversation], conversationId).asInstanceOf[Conversation]
    assertEquals(conversation.participantsUserIds.length, 4)

    participantBConversationInfo = conversationInfoDatabase.getConversationInfo(participantBUserId, conversationId).get
    assertTrue(participantBConversationInfo.deleted)
    assertFalse(participantBConversationInfo.participating)
    

    // Third participant lefts conversation
    var participantCConversationInfo = conversationInfoDatabase.getConversationInfo(participantCUserId, conversationId).get
    conversationCommand.exitConversation(participantCUserId, participantCConversationInfo.id, participantCConversationInfo.version)

    participantCConversationInfo = conversationInfoDatabase.getConversationInfo(participantCUserId, conversationId).get
    assertFalse(participantCConversationInfo.deleted)
    assertFalse(participantCConversationInfo.participating)


    // Third participant comes back to conversation
    conversationCommand.enterConversation(participantCUserId, participantCConversationInfo.id, participantCConversationInfo.version)

    participantCConversationInfo = conversationInfoDatabase.getConversationInfo(participantCUserId, conversationId).get
    assertFalse(participantCConversationInfo.deleted)
    assertTrue(participantCConversationInfo.participating)
  }
}
