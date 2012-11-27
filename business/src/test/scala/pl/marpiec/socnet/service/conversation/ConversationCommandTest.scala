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
    assertEquals(conversation.invitedUserIds.size, 2)
    assertEquals(conversation.participantsUserIds.size, 1)
    assertEquals(conversation.messages.size, 1)

    val conversationInfoForParticipantAOption = conversationInfoDatabase.getConversationInfo(participantAUserId, conversationId)
    assertTrue(conversationInfoForParticipantAOption.isDefined)

    // First User reads conversation

    val conversationInfoForParticipantA = conversationInfoForParticipantAOption.get

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

    // accepting invitation by participant B

    conversationCommand.enterConversation(participantBUserId, conversationId, conversation.version, participantBUserId)
    conversation = dataStore.getEntity(classOf[Conversation], conversationId).asInstanceOf[Conversation]

    assertEquals(conversation.invitedUserIds.size, 1)
    assertEquals(conversation.participantsUserIds.size, 2)
    assertFalse(conversation.invitedUserIds.contains(participantBUserId))
    assertTrue(conversation.participantsUserIds.contains(participantBUserId))

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
    conversationCommand.addParticipants(participantBUserId, conversationId, conversation.version, List(participantDUserId))

    conversation = dataStore.getEntity(classOf[Conversation], conversationId).asInstanceOf[Conversation]
    assertEquals(conversation.invitedUserIds.size, 2)
    assertEquals(conversation.participantsUserIds.size, 2)

    // Second participant removes conversation from his conversations


    conversationCommand.removeConversationForUser(participantBUserId, conversationId, conversation.version, participantBUserId)
    conversation = dataStore.getEntity(classOf[Conversation], conversationId).asInstanceOf[Conversation]
    assertEquals(conversation.participantsUserIds.size, 1)
    assertEquals(conversation.invitedUserIds.size, 2)

    // Third participant enters conversation

    conversationCommand.enterConversation(participantCUserId, conversationId, conversation.version, participantCUserId)
    conversation = dataStore.getEntity(classOf[Conversation], conversationId).asInstanceOf[Conversation]

    assertEquals(conversation.participantsUserIds.size, 2)
    assertEquals(conversation.invitedUserIds.size, 1)
    assertFalse(conversation.invitedUserIds.contains(participantCUserId))
    assertTrue(conversation.participantsUserIds.contains(participantCUserId))

    // Third participant lefts conversation

    conversationCommand.exitConversation(participantCUserId, conversationId, conversation.version, participantCUserId)
    conversation = dataStore.getEntity(classOf[Conversation], conversationId).asInstanceOf[Conversation]

    assertEquals(conversation.participantsUserIds.size, 1)
    assertEquals(conversation.invitedUserIds.size, 2)
    assertTrue(conversation.invitedUserIds.contains(participantCUserId))
    assertFalse(conversation.participantsUserIds.contains(participantCUserId))


    // Third participant comes back to conversation
    conversationCommand.enterConversation(participantCUserId, conversationId, conversation.version, participantCUserId)
    conversation = dataStore.getEntity(classOf[Conversation], conversationId).asInstanceOf[Conversation]

    assertEquals(conversation.participantsUserIds.size, 2)
    assertEquals(conversation.invitedUserIds.size, 1)
    assertFalse(conversation.invitedUserIds.contains(participantCUserId))
    assertTrue(conversation.participantsUserIds.contains(participantCUserId))
  }
}
