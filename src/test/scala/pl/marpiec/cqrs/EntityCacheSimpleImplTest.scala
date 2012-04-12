package pl.marpiec.cqrs

import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.socnet.model.User
import java.util.UUID

/**
 * @author Marcin Pieciukiewicz
 */
@Test
class EntityCacheSimpleImplTest {
  def testSimpleCacheOperations() {

    val entityCache:EntityCache = new EntityCacheSimpleImpl

    if(entityCache.get(classOf[User], UUID.randomUUID()).isDefined) {
      fail("Unexpeced entity in database")
    }
    
    val user = new User
    val userUid = UUID.randomUUID();
    user.uuid = userUid
    user.name = "Marcin"
    
    entityCache.put(user)
    val userFromCacheOption = entityCache.get(classOf[User], userUid).asInstanceOf[Option[User]]

    if(userFromCacheOption.isEmpty) {
      fail("user not found")
    }

    assertEquals(userFromCacheOption.get.name, "Marcin")

  }
}
