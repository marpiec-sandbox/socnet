package pl.marpiec.cqrs

import collection.mutable.{ListBuffer, HashMap}
import pl.marpiec.util.UID


class DataStoreImpl (val eventStore:EventStore, val entityCache:EntityCache) extends EventStoreListener with DataStore {

  private val listeners = new HashMap[Class[_ <: CqrsEntity], ListBuffer[DataStoreListener]]

  startListeningToEventStore(eventStore)

  def getEntity(entityClass:Class[_ <: CqrsEntity], id: UID):CqrsEntity = {
    val entity = getNewOrCachedEntity(entityClass, id)

    //TODO doadac wyciaganie zakresu eventow
    val events = eventStore.getEventsForEntity(entityClass, id)

    events.foreach((event) => {
        if(event.expectedVersion == entity.version) {
          event.event.applyEvent(entity)
          entity.version = entity.version + 1
        }
      }
    )
    entity

  }

  private def getNewOrCachedEntity(entityClass: Class[_ <: CqrsEntity], id: UID):CqrsEntity = {
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

  def addListener(entityClass: Class[_ <: CqrsEntity], listener: DataStoreListener) {
    val entityListeners = listeners.getOrElseUpdate(entityClass, new ListBuffer[DataStoreListener])
    entityListeners += listener
  }

  def onEntityChanged(entityClass:Class[_ <: CqrsEntity], entityId:UID) {

    val entity = getEntity(entityClass, entityId:UID)

    val entityListenersOption = listeners.get(entityClass)
    
    if(entityListenersOption.isDefined) {
      entityListenersOption.get.foreach(listener => listener.onEntityChanged(entity))
    }
  }
}
