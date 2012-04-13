package pl.marpiec.cqrs

import collection.mutable.{ListBuffer, HashMap, WeakHashMap}
import pl.marpiec.util.UID


/**
 * @author Marcin Pieciukiewicz
 */

class EntityCacheSimpleImpl extends EntityCache {
  
  private val cache = new WeakHashMap[Class[_], WeakHashMap[UID, CqrsEntity]]
  
  def get(entityClass: Class[_ <: CqrsEntity], id: UID):Option[CqrsEntity] = {
    var entitiesForType = cache.get(entityClass)
    entitiesForType match {
      case Some(entities) => {
        entities.get(id)
      }
      case None => {
        None
      }
    }
  }

  def put(entity: CqrsEntity) {
    var entitiesForType = cache.getOrElseUpdate(entity.getClass, new WeakHashMap[UID, CqrsEntity])
    entitiesForType += entity.id -> entity
  }
}
