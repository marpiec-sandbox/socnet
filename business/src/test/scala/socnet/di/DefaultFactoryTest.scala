package pl.marpiec.socnet.di

import org.testng.annotations.Test
import org.testng.Assert._

@Test
class DefaultFactoryTest {
   def testComponentCreation() {

     assertNotNull(Factory.dataStore)
     assertNotNull(Factory.eventStore)

     assertNotNull(Factory.userDatabase)
     assertNotNull(Factory.articleDatabase)
     assertNotNull(Factory.userProfileDatabase)

     assertNotNull(Factory.userCommand)
     assertNotNull(Factory.userQuery)

     assertNotNull(Factory.articleCommand)
   }
}
