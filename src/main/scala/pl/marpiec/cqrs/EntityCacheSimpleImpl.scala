package pl.marpiec.cqrs

import collection.mutable.{ListBuffer, HashMap, WeakHashMap}
import java.util.UUID


/**
 * @author Marcin Pieciukiewicz
 */

class EntityCacheSimpleImpl extends EntityCache {
  
  private val cache = new WeakHashMap[Class[_], WeakHashMap[UUID, CqrsEntity]]
  
  def get(entityClass: Class[_ <: CqrsEntity], uuid: UUID):Option[CqrsEntity] = {
    var entitiesForType = cache.get(entityClass)
    entitiesForType match {
      case Some(entities) => {
        entities.get(uuid)
      }
      case None => {
        None
      }
    }
  }

  def put(entity: CqrsEntity) {
    var entitiesForType = cache.getOrElseUpdate(entity.getClass, new WeakHashMap[UUID, CqrsEntity])
    entitiesForType += entity.uuid -> entity
  }
}
