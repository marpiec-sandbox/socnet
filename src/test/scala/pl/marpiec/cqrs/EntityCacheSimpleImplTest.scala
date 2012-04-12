package pl.marpiec.cqrs

import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.socnet.model.User
import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */
@Test
class EntityCacheSimpleImplTest {
  def testSimpleCacheOperations() {

    val entityCache:EntityCache = new EntityCacheSimpleImpl

    if(entityCache.get(classOf[User], UID.generate).isDefined) {
      fail("Unexpeced entity in database")
    }
    
    val user = new User
    val userUid = UID.generate;
    user.id = userUid
    user.name = "Marcin"
    
    entityCache.put(user)
    val userFromCacheOption = entityCache.get(classOf[User], userUid).asInstanceOf[Option[User]]

    if(userFromCacheOption.isEmpty) {
      fail("user not found")
    }

    assertEquals(userFromCacheOption.get.name, "Marcin")

  }
}
