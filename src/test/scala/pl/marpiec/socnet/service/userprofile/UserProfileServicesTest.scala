package pl.marpiec.socnet.service.userprofile

import input.{JobExperienceParam, PersonalSummary}
import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.cqrs._
import pl.marpiec.socnet.database.{ArticleDatabaseMockImpl, ArticleDatabase}
import pl.marpiec.socnet.service.article.{ArticleCommandImpl, ArticleCommand}
import pl.marpiec.socnet.model.UserProfile
import org.joda.time.LocalDate
import pl.marpiec.socnet.model.userprofile.JobExperience
import java.util.UUID

/**
 * @author Marcin Pieciukiewicz
 */

@Test
class UserProfileServicesTest {

  def testProfileCreationAndModification() {
    val eventStore: EventStore = new EventStoreImpl
    val entityCache: EntityCache = new EntityCacheSimpleImpl
    val dataStore: DataStore = new DataStoreImpl(eventStore, entityCache)
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
  
  
  def testExperienceModification() {
    val eventStore: EventStore = new EventStoreImpl
    val entityCache: EntityCache = new EntityCacheSimpleImpl
    val dataStore: DataStore = new DataStoreImpl(eventStore, entityCache)
    val userProfileCommand: UserProfileCommand = new UserProfileCommandImpl(eventStore, dataStore)

    val userId = 1
    val userProfileId = userProfileCommand.createUserProfile(userId)
    var userProfile = dataStore.getEntity(classOf[UserProfile], userProfileId).asInstanceOf[UserProfile]
    
    var jobExperienceParam = new JobExperienceParam

    jobExperienceParam.companyName = "socnet"
    jobExperienceParam.description = "coding and programming"
    jobExperienceParam.startDateOption = Option[LocalDate](new LocalDate(2002, 10, 1))
    jobExperienceParam.endDateOption = Option[LocalDate](new LocalDate(2006, 3, 1))
    jobExperienceParam.position = "Programmer"
    jobExperienceParam.uuid = UUID.randomUUID()

    val jobExperienceUuid = jobExperienceParam.uuid
    userProfileCommand.addJobExperience(userProfile.id, userProfile.version, jobExperienceParam)

    userProfile = dataStore.getEntity(classOf[UserProfile], userProfileId).asInstanceOf[UserProfile]

    assertEquals(userProfile.jobExperience.size, 1)
    var experience = userProfile.jobExperience.head
    assertEquals(experience.companyName, "socnet")
    assertEquals(experience.description, "coding and programming")
    assertEquals(experience.startDateOption.get, new LocalDate(2002, 10, 1))
    assertEquals(experience.endDateOption.get, new LocalDate(2006, 3, 1))
    assertEquals(experience.position, "Programmer")


    jobExperienceParam = new JobExperienceParam

    jobExperienceParam.uuid = jobExperienceUuid
    jobExperienceParam.companyName = "socnet"
    jobExperienceParam.description = "coding and programming"
    jobExperienceParam.startDateOption = Option[LocalDate](new LocalDate(2002, 10, 1))
    jobExperienceParam.endDateOption = Option[LocalDate](new LocalDate(2006, 3, 1))
    jobExperienceParam.position = "CTO"

    userProfileCommand.updateJobExperience(userProfile.id, userProfile.version, jobExperienceParam)

    userProfile = dataStore.getEntity(classOf[UserProfile], userProfileId).asInstanceOf[UserProfile]

    assertEquals(userProfile.jobExperience.size, 1)
    experience = userProfile.jobExperience.head
    assertEquals(experience.companyName, "socnet")
    assertEquals(experience.description, "coding and programming")
    assertEquals(experience.startDateOption.get, new LocalDate(2002, 10, 1))
    assertEquals(experience.endDateOption.get, new LocalDate(2006, 3, 1))
    assertEquals(experience.position, "CTO")

    userProfileCommand.removeJobExperience(userProfile.id, userProfile.version, jobExperienceUuid)

    userProfile = dataStore.getEntity(classOf[UserProfile], userProfileId).asInstanceOf[UserProfile]

    assertEquals(userProfile.jobExperience.size, 0)
    
  }
}
