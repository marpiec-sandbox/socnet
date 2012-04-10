package pl.marpiec.socnet.database


import org.testng.annotations.Test
import pl.marpiec.cqrs.{EntityCacheSimpleImpl, EventStoreImpl, DataStoreImpl}
import pl.marpiec.socnet.model.UserProfile
import pl.marpiec.socnet.model.userprofile.Education
import org.joda.time.LocalDate
import org.testng.Assert._

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

@Test
class UserProfileDatabaseMockImplTest {

  def testBasicDatabaseOperations() {
    val userProfileDatabase: UserProfileDatabase = new UserProfileDatabaseMockImpl(new DataStoreImpl(new EventStoreImpl, new EntityCacheSimpleImpl))

    val profile = new UserProfile
    profile.id = 1
    profile.userId = 2
    profile.city = "Warszawa"
    profile.province = "Mazowieckie"
    profile.wwwPage = "socnet.pl"

    var education = new Education
    education.schoolName = "Politechnika Warszawska"
    education.startDateOption = Option(new LocalDate(2002, 10, 1))
    education.endDateOption = Option(new LocalDate(2008, 6, 1))
    education.major = "Elektronika i Inżynieria Komputerowa"
    education.level = "Magisterskie"

    profile.education += education

    userProfileDatabase.addUserProfile(profile)
    val userProfileOption = userProfileDatabase.getUserProfileById(1)
    val userProfileOptionByUserId = userProfileDatabase.getUserProfileByUserId(2)

    assertTrue(userProfileOption.isDefined)
    assertTrue(userProfileOptionByUserId.isDefined)

    assertEquals(userProfileOption.get.id, userProfileOptionByUserId.get.id)

    val userProfileFromDatabase = userProfileOption.get

    assertTrue(profile ne userProfileFromDatabase)
    assertEquals(userProfileFromDatabase.province, profile.province)
    assertEquals(userProfileFromDatabase.wwwPage, profile.wwwPage)
    assertEquals(userProfileFromDatabase.education.size, profile.education.size)

  }
}
