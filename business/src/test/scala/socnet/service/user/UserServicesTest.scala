package pl.marpiec.socnet.service.user

import pl.marpiec.socnet.database.{UserDatabaseMockImpl, UserDatabase}
import pl.marpiec.cqrs._
import exception.ConcurrentAggregateModificationException
import org.testng.Assert._
import org.testng.annotations.Test
import pl.marpiec.socnet.model.User
import pl.marpiec.util.UID
import pl.marpiec.mailsender.{MailSender, MailSenderMockImpl}
import socnet.template.{TemplateRepositoryMockImpl, TemplateRepository}

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

@Test
class UserServicesTest {

  val eventStore:EventStore = new EventStoreMockImpl
  val entityCache:AggregateCache = new AggregateCacheSimpleImpl
  val dataStore:DataStore = new DataStoreImpl(eventStore, entityCache)
  val userDatabase:UserDatabase = new UserDatabaseMockImpl(dataStore)
  val uidGenerator:UidGenerator = new UidGeneratorMockImpl
  val triggeredEvents:TriggeredEvents = new TriggeredEventsMockImpl
  val mailSender:MailSender = new MailSenderMockImpl
  val templateRepository:TemplateRepository = new TemplateRepositoryMockImpl

  val userCommand:UserCommand = new UserCommandImpl(eventStore, dataStore, triggeredEvents, userDatabase, uidGenerator, mailSender, templateRepository)

  val trigger = userCommand.createRegisterUserTrigger("Marcin", "Pieciukiewicz", "m.pieciukiewicz@socnet", "Haslo");
  val userId = userCommand.triggerUserRegistration(trigger)

  var user = dataStore.getEntity(classOf[User], userId).asInstanceOf[User]

  assertEquals(user.firstName, "Marcin")
  assertEquals(user.lastName, "Pieciukiewicz")
  assertEquals(user.displayName, "Marcin Pieciukiewicz")
  assertEquals(user.email, "m.pieciukiewicz@socnet")

  userCommand.changeUserEmail(new UID(0), userId, user.version, "m.pieciukiewicz@socnet.org")

  user = dataStore.getEntity(classOf[User], userId).asInstanceOf[User]

  assertEquals(user.firstName, "Marcin")
  assertEquals(user.lastName, "Pieciukiewicz")
  assertEquals(user.displayName, "Marcin Pieciukiewicz")
  assertEquals(user.email, "m.pieciukiewicz@socnet.org")

  try {
    userCommand.changeUserEmail(new UID(0), userId, user.version-1, "irek@socnet")
    fail("ConcurrentAggregateModificationException should be thrown")
  } catch {
    case e:ConcurrentAggregateModificationException => {}
    case e:Exception => {
      fail("Caught illegal exception, ConcurrentAggregateModificationException expected");
    }
  }
}
