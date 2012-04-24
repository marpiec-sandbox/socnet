package pl.marpiec.cqrs

import collection.mutable.{ListBuffer, HashMap, WeakHashMap}
import pl.marpiec.util.UID


/**
 * @author Marcin Pieciukiewicz
 */

class AggregateCacheSimpleImpl extends AggregateCache {
  
  private val cache = new WeakHashMap[Class[_], WeakHashMap[UID, Aggregate]]
  
  def get(entityClass: Class[_ <: Aggregate], id: UID):Option[Aggregate] = {
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

  def put(entity: Aggregate) {
    var entitiesForType = cache.getOrElseUpdate(entity.getClass, new WeakHashMap[UID, Aggregate])
    entitiesForType += entity.id -> entity
  }
}
