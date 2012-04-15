package pl.marpiec.cqrs

import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

@Test
class EntityCacheSimpleImplTest {
  def testSimpleCacheOperations() {

    val entityCache:EntityCache = new EntityCacheSimpleImpl
    val uidGenerator:UidGenerator = new UidGeneratorMockImpl

    if(entityCache.get(classOf[SimpleTestEntity], uidGenerator.nextUid).isDefined) {
      fail("Unexpeced entity in database")
    }
    
    val entity = new SimpleTestEntity
    val entityId = uidGenerator.nextUid;
    entity.id = entityId
    entity.name = "Marcin"
    
    entityCache.put(entity)
    val userFromCacheOption = entityCache.get(classOf[SimpleTestEntity], entityId).asInstanceOf[Option[SimpleTestEntity]]

    if(userFromCacheOption.isEmpty) {
      fail("user not found")
    }

    assertEquals(userFromCacheOption.get.name, "Marcin")

  }
}
