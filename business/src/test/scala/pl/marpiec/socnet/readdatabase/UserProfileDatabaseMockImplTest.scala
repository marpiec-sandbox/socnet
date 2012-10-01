package pl.marpiec.socnet.readdatabase


import mock.UserProfileDatabaseMockImpl
import org.testng.annotations.Test
import pl.marpiec.socnet.model.UserProfile
import pl.marpiec.socnet.model.userprofile.Education
import org.testng.Assert._
import pl.marpiec.cqrs._
import pl.marpiec.socnet.constant.{Month, Province}

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

@Test
class UserProfileDatabaseMockImplTest {

  def testBasicDatabaseOperations() {
    val userProfileDatabase: UserProfileDatabase = new UserProfileDatabaseMockImpl(new DataStoreImpl(new EventStoreMockImpl, new AggregateCacheSimpleImpl))
    val uidGenerator: UidGenerator = new UidGeneratorMockImpl

    val profileId = uidGenerator.nextUid
    val userId = uidGenerator.nextUid

    val profile = new UserProfile
    profile.id = profileId
    profile.userId = userId
    profile.city = "Warszawa"
    profile.province = Province.MAZOWIECKIE
    profile.wwwPage = "socnet.pl"

    var education = new Education
    education.schoolName = "Politechnika Warszawska"
    education.fromYear = 2002
    education.fromMonthOption = Option[Month](Month.OCTOBER)
    education.toYear = 2008
    education.toMonthOption = Option[Month](Month.JUNE)
    education.major = "Elektronika i Inżynieria Komputerowa"
    education.level = "Magisterskie"

    profile.education ::= education

    userProfileDatabase.addUserProfile(profile)
    val userProfileOption = userProfileDatabase.getUserProfileById(profileId)
    val userProfileOptionByUserId = userProfileDatabase.getUserProfileByUserId(userId)

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
