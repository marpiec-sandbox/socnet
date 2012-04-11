package pl.marpiec.socnet.service.userprofile

import input.PersonalSummary
import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.cqrs._
import pl.marpiec.socnet.database.{ArticleDatabaseMockImpl, ArticleDatabase}
import pl.marpiec.socnet.service.article.{ArticleCommandImpl, ArticleCommand}
import pl.marpiec.socnet.model.UserProfile

/**
 * @author Marcin Pieciukiewicz
 */

@Test
class UserProfileServicesTest {

  def testProfileCreationAndModification() {
    val eventStore: EventStore = new EventStoreImpl
    val entityCache: EntityCache = new EntityCacheSimpleImpl
    val dataStore: DataStore = new DataStoreImpl(eventStore, entityCache)
    val userProfileDatabase: ArticleDatabase = new ArticleDatabaseMockImpl(dataStore)

    val userProfileCommand: UserProfileCommand = new UserProfileCommandImpl(eventStore, dataStore)

    val userId = 1
    val userProfileId = userProfileCommand.createUserProfile(userId)

    var userProfile = dataStore.getEntity(classOf[UserProfile], userProfileId).asInstanceOf[UserProfile]

    val personalSummary = new PersonalSummary

    personalSummary.professionalTitle = "Mr. "
    personalSummary.city = "Warszawa"
    personalSummary.province = "mazowieckie"
    personalSummary.blogPage = ""
    personalSummary.wwwPage = "socnet.pl"

    userProfileCommand.updatePersonalSummary(userProfile.id, userProfile.version, personalSummary)

    userProfile = dataStore.getEntity(classOf[UserProfile], userProfileId).asInstanceOf[UserProfile]

    assertEquals(userProfile.userId, userId)
    assertEquals(userProfile.professionalTitle, personalSummary.professionalTitle)
    assertEquals(userProfile.city, personalSummary.city)
    assertEquals(userProfile.province, personalSummary.province)
    assertEquals(userProfile.blogPage, personalSummary.blogPage)
    assertEquals(userProfile.wwwPage, personalSummary.wwwPage)




  }
}
