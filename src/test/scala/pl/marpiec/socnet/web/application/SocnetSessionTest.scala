package pl.marpiec.socnet.web.application

import org.testng.annotations.Test
import org.testng.Assert._
import org.apache.wicket.mock.MockWebRequest
import org.apache.wicket.request.Url
import pl.marpiec.socnet.di.Factory
import pl.marpiec.socnet.service.user.UserCommandImpl
import pl.marpiec.cqrs.{EntityCacheSimpleImpl, DataStoreImpl, DataStore, EventStoreImpl}
import pl.marpiec.socnet.database.UserDatabaseMockImpl

@Test
class SocnetSessionTest {

   def testSimpleLogin() {
     val eventStore = new EventStoreImpl
     val entityCache = new EntityCacheSimpleImpl
     val dataStore = new DataStoreImpl(eventStore, entityCache)
     val userDatabase = new UserDatabaseMockImpl(dataStore)
     val userCommand = new UserCommandImpl(eventStore, dataStore, userDatabase)

     val socnetSession = new SocnetSession(new MockWebRequest(new Url()))

     userCommand.registerUser("Marcin Pieciukiewicz", "m.pieciukiewicz@socnet", "Haslo")

     var authenticationResult = socnetSession.authenticate("m.pieciukiewicz@socnet", "nie znam hasla")
     
     assertFalse(authenticationResult, "user shuldn't be able to authenticate")

     assertNull(socnetSession.user, "There shoudn't be user data if authentication failed")

     authenticationResult = socnetSession.authenticate("m.pieciukiewicz@socnet", "Haslo")

     assertTrue(authenticationResult, "user should be able to authenticate")

     assertEquals(socnetSession.user.name, "Marcin Pieciukiewicz")

     socnetSession.clearSessionData

     assertNull(socnetSession.user, "there should be no data in the session now")

   }
}
