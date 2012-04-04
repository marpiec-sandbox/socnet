package pl.marpiec.cqrs

import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.socnet.model.User

/**
 * @author Marcin Pieciukiewicz
 */
@Test
class EntityCacheSimpleImplTest {
  def testSimpleCacheOperations() {

    val entityCache:EntityCache = new EntityCacheSimpleImpl

    if(entityCache.get(classOf[User], 1).isDefined) {
      fail("Unexpeced entity in database")
    }
    
    val user = new User
    user.id = 1
    user.name = "Marcin"
    
    entityCache.put(user)
    val userFromCacheOption = entityCache.get(classOf[User], 1).asInstanceOf[Option[User]]

    if(userFromCacheOption.isEmpty) {
      fail("user not found")
    }

    assertEquals(userFromCacheOption.get.name, "Marcin")

  }
}
