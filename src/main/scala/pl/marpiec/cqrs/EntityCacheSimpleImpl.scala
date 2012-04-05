package pl.marpiec.cqrs

import collection.mutable.{ListBuffer, HashMap, WeakHashMap}


/**
 * @author Marcin Pieciukiewicz
 */

class EntityCacheSimpleImpl extends EntityCache {
  
  private val cache = new WeakHashMap[Class[_], WeakHashMap[Int, CqrsEntity]]
  
  def get(entityClass: Class[_ <: CqrsEntity], id: Int):Option[CqrsEntity] = {
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
    var entitiesForType = cache.getOrElseUpdate(entity.getClass, new WeakHashMap[Int, CqrsEntity])
    entitiesForType += entity.id -> entity
  }
}
