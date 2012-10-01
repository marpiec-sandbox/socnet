package pl.marpiec.socnet.readdatabase

import mock.ConversationDatabaseMockImpl
import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.cqrs._
import pl.marpiec.socnet.model.Conversation

/**
 * @author Marcin Pieciukiewicz
 */

@Test
class ConversationDatabaseMockImplTest {

  def testBasicDatabaseOperations() {

    val eventStore: EventStore = new EventStoreMockImpl
    val entityCache: AggregateCache = new AggregateCacheSimpleImpl
    val dataStore: DataStore = new DataStoreImpl(eventStore, entityCache)
    val conversationDatabase: ConversationDatabase = new ConversationDatabaseMockImpl(dataStore)

    val uidGenerator: UidGenerator = new UidGeneratorMockImpl

    val conversationId = uidGenerator.nextUid
    val participantAUserId = uidGenerator.nextUid
    val participantBUserId = uidGenerator.nextUid
    val nonParticipantCUserId = uidGenerator.nextUid

    val conversation = new Conversation
    conversation.id = conversationId
    conversation.title = "Database test conversation"
    conversation.participantsUserIds = participantAUserId :: participantBUserId :: Nil
    conversation.creatorUserId = participantAUserId

    conversationDatabase.addConversation(conversation)

    val conversationOption = conversationDatabase.getConversationById(conversationId)
    assertTrue(conversationOption.isDefined)
    assertEquals(conversationOption.get.id, conversationId)
    assertEquals(conversationOption.get.title, "Database test conversation")

    val conversationsOfA = conversationDatabase.getConversationsByParticipantUserId(participantAUserId)
    val conversationsOfB = conversationDatabase.getConversationsByParticipantUserId(participantBUserId)
    val conversationsOfC = conversationDatabase.getConversationsByParticipantUserId(nonParticipantCUserId)

    assertEquals(conversationsOfA.size, 1)
    assertEquals(conversationsOfB.size, 1)
    assertEquals(conversationsOfC.size, 0)
  }

}
