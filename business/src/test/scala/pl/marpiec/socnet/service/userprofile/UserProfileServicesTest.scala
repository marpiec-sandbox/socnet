package pl.marpiec.socnet.service.userprofile

import input.PersonalSummary
import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.util.UID
import scala.Option
import pl.marpiec.socnet.constant.{Province, Month}
import pl.marpiec.socnet.model.userprofile.{Education, JobExperience}
import org.apache.commons.lang.StringUtils
import pl.marpiec.cqrs._
import pl.marpiec.socnet.model.UserProfile

/**
 * @author Marcin Pieciukiewicz
 */

@Test
class UserProfileServicesTest {

  def testProfileCreationAndModification() {
    val eventStore: EventStore = new EventStoreMockImpl
    val entityCache: AggregateCache = new AggregateCacheSimpleImpl
    val dataStore: DataStore = new DataStoreImpl(eventStore, entityCache)
    val uidGenerator: UidGenerator = new UidGeneratorMockImpl
    val userProfileCommand: UserProfileCommand = new UserProfileCommandImpl(eventStore)

    val userId = uidGenerator.nextUid
    val userProfileId = uidGenerator.nextUid

    userProfileCommand.createUserProfile(new UID(0), userId, userProfileId)

    var userProfile = dataStore.getEntity(classOf[UserProfile], userProfileId).asInstanceOf[UserProfile]

    val personalSummary = new PersonalSummary

    personalSummary.city = "Warszawa"
    personalSummary.province = Province.MAZOWIECKIE
    personalSummary.blogPage = ""
    personalSummary.wwwPage = "socnet.pl"

    userProfileCommand.updatePersonalSummary(new UID(0), userProfile.id, userProfile.version, personalSummary)

    userProfile = dataStore.getEntity(classOf[UserProfile], userProfileId).asInstanceOf[UserProfile]

    assertEquals(userProfile.userId, userId)
    assertEquals(userProfile.city, personalSummary.city)
    assertEquals(userProfile.province, personalSummary.province)
    assertEquals(userProfile.blogPage, personalSummary.blogPage)
    assertEquals(userProfile.wwwPage, personalSummary.wwwPage)

  }


  def testExperienceModification() {
    val eventStore: EventStore = new EventStoreMockImpl
    val entityCache: AggregateCache = new AggregateCacheSimpleImpl
    val dataStore: DataStore = new DataStoreImpl(eventStore, entityCache)
    val uidGenerator: UidGenerator = new UidGeneratorMockImpl
    val userProfileCommand: UserProfileCommand = new UserProfileCommandImpl(eventStore)

    val userId = uidGenerator.nextUid
    val userProfileId = uidGenerator.nextUid
    userProfileCommand.createUserProfile(new UID(0), userId, userProfileId)
    var userProfile = dataStore.getEntity(classOf[UserProfile], userProfileId).asInstanceOf[UserProfile]

    var jobExperience = new JobExperience

    jobExperience.companyName = "socnet"
    jobExperience.description = "coding and programming"
    jobExperience.fromYear = 2002
    jobExperience.fromMonthOption = Option(Month.OCTOBER)

    jobExperience.toYear = 2006
    jobExperience.toMonthOption = Option(Month.MARCH)

    jobExperience.position = "Programmer"
    val jobExperienceUid = uidGenerator.nextUid
    jobExperience.id = jobExperienceUid

    userProfileCommand.addJobExperience(new UID(0), userProfile.id, userProfile.version, jobExperience, jobExperienceUid)

    userProfile = dataStore.getEntity(classOf[UserProfile], userProfileId).asInstanceOf[UserProfile]

    assertEquals(userProfile.jobExperience.size, 1)
    var experience = userProfile.jobExperience.head
    assertEquals(experience.companyName, "socnet")
    assertEquals(experience.description, "coding and programming")
    assertEquals(experience.fromYear, 2002)
    assertEquals(experience.fromMonthOption.get, Month.OCTOBER)
    assertEquals(experience.toYear, 2006)
    assertEquals(experience.toMonthOption.get, Month.MARCH)
    assertEquals(experience.position, "Programmer")


    jobExperience = new JobExperience

    jobExperience.id = jobExperienceUid
    jobExperience.companyName = "socnet"
    jobExperience.description = "coding and programming"
    jobExperience.fromYear = 2002
    jobExperience.fromMonthOption = Option(Month.OCTOBER)

    jobExperience.toYear = 2006
    jobExperience.toMonthOption = Option(Month.MARCH)
    jobExperience.position = "CTO"

    userProfileCommand.updateJobExperience(new UID(0), userProfile.id, userProfile.version, jobExperience)

    userProfile = dataStore.getEntity(classOf[UserProfile], userProfileId).asInstanceOf[UserProfile]

    assertEquals(userProfile.jobExperience.size, 1)
    experience = userProfile.jobExperience.head
    assertEquals(experience.companyName, "socnet")
    assertEquals(experience.description, "coding and programming")
    assertEquals(experience.fromYear, 2002)
    assertEquals(experience.fromMonthOption.get, Month.OCTOBER)
    assertEquals(experience.toYear, 2006)
    assertEquals(experience.toMonthOption.get, Month.MARCH)
    assertEquals(experience.position, "CTO")

    userProfileCommand.removeJobExperience(new UID(0), userProfile.id, userProfile.version, jobExperienceUid)

    userProfile = dataStore.getEntity(classOf[UserProfile], userProfileId).asInstanceOf[UserProfile]

    assertEquals(userProfile.jobExperience.size, 0)

  }

  def testEducationModification() {
    val eventStore: EventStore = new EventStoreMockImpl
    val entityCache: AggregateCache = new AggregateCacheSimpleImpl
    val dataStore: DataStore = new DataStoreImpl(eventStore, entityCache)
    val uidGenerator: UidGenerator = new UidGeneratorMockImpl
    val userProfileCommand: UserProfileCommand = new UserProfileCommandImpl(eventStore)

    val userId = uidGenerator.nextUid
    val userProfileId = uidGenerator.nextUid
    userProfileCommand.createUserProfile(new UID(0), userId, userProfileId)
    var userProfile = dataStore.getEntity(classOf[UserProfile], userProfileId).asInstanceOf[UserProfile]

    var education = new Education
    education.schoolName = "Politechnika Warszawska"
    education.level = "Magisterskie"
    education.major = "Elektronika i inzynieria komputerowa"
    education.fromYear = 2002
    education.fromMonthOption = Option[Month](Month.SEPTEMBER)
    education.toYear = 2009
    education.toMonthOption = Option[Month](Month.JUNE)
    education.stillStudying = false

    val educationUid = uidGenerator.nextUid
    education.id = educationUid

    userProfileCommand.addEducation(new UID(0), userProfile.id, userProfile.version, education, educationUid)

    userProfile = dataStore.getEntity(classOf[UserProfile], userProfileId).asInstanceOf[UserProfile]

    assertEquals(userProfile.education.size, 1)
    var loadedEducation = userProfile.education.head
    assertEquals(loadedEducation.schoolName, "Politechnika Warszawska")
    assertEquals(loadedEducation.level, "Magisterskie")
    assertEquals(loadedEducation.major, "Elektronika i inzynieria komputerowa")
    assertTrue(StringUtils.isEmpty(loadedEducation.description))
    assertEquals(loadedEducation.fromYear, 2002)
    assertEquals(loadedEducation.fromMonthOption.get, Month.SEPTEMBER)
    assertEquals(loadedEducation.toYear, 2009)
    assertEquals(loadedEducation.toMonthOption.get, Month.JUNE)
    assertEquals(loadedEducation.stillStudying, false)

    education.level = "Magistersko - Inzynieryjne"
    education.stillStudying = true
    userProfileCommand.updateEducation(new UID(0), userProfile.id, userProfile.version, education)

    userProfile = dataStore.getEntity(classOf[UserProfile], userProfileId).asInstanceOf[UserProfile]

    assertEquals(userProfile.education.size, 1)
    loadedEducation = userProfile.education.head
    assertEquals(loadedEducation.schoolName, "Politechnika Warszawska")
    assertEquals(loadedEducation.level, "Magistersko - Inzynieryjne")
    assertEquals(loadedEducation.major, "Elektronika i inzynieria komputerowa")
    assertTrue(StringUtils.isEmpty(loadedEducation.description))
    assertEquals(loadedEducation.fromYear, 2002)
    assertEquals(loadedEducation.fromMonthOption.get, Month.SEPTEMBER)
    assertEquals(loadedEducation.toYear, 0)
    assertTrue(loadedEducation.toMonthOption.isEmpty)
    assertEquals(loadedEducation.stillStudying, true)

    userProfileCommand.removeEducation(new UID(0), userProfile.id, userProfile.version, educationUid)

    userProfile = dataStore.getEntity(classOf[UserProfile], userProfileId).asInstanceOf[UserProfile]

    assertEquals(userProfile.education.size, 0)
  }
}
