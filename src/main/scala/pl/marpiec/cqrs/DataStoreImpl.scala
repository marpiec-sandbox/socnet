package pl.marpiec.cqrs

class DataStoreImpl (val eventStore:EventStore, val entityCache:EntityCache) extends DataStore {

  

  def getEntity(entityClass:Class[_ <: CqrsEntity], id: Int) = {
    val entity = getNewOrCachedEntity(entityClass, id)

    //TODO doadac wyciaganie zakresu eventow
    val events = eventStore.getEventsForEntity(entityClass, id)

    events.foreach((event) => {
        if(event.expectedVersion == entity.version) {
          event.applyEvent(entity)
          entity.version = entity.version + 1
        }
      }
    )

    entity

  }


  def getNewOrCachedEntity(entityClass: Class[_ <: CqrsEntity], id: Int):CqrsEntity = {
    entityCache.get(entityClass, id) match {
      case Some(entity) => entity
      case None => {
        val entity: CqrsEntity = entityClass.newInstance
        entity.id = id
        entity.version = 0
        entity
      }
    }
  }
  
}
