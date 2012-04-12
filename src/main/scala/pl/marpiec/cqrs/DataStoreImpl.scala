package pl.marpiec.cqrs

import collection.mutable.{ListBuffer, HashMap}
import java.util.UUID


class DataStoreImpl (val eventStore:EventStore, val entityCache:EntityCache) extends EventStoreListener with DataStore {

  private val listeners = new HashMap[Class[_ <: CqrsEntity], ListBuffer[DataStoreListener]]

  startListeningToEventStore(eventStore)

  def getEntity(entityClass:Class[_ <: CqrsEntity], uuid: UUID):CqrsEntity = {
    val entity = getNewOrCachedEntity(entityClass, uuid)

    //TODO doadac wyciaganie zakresu eventow
    val events = eventStore.getEventsForEntity(entityClass, uuid)

    events.foreach((event) => {
        if(event.expectedVersion == entity.version) {
          event.applyEvent(entity)
          entity.version = entity.version + 1
        }
      }
    )

    entity

  }

  private def getNewOrCachedEntity(entityClass: Class[_ <: CqrsEntity], uuid: UUID):CqrsEntity = {
    entityCache.get(entityClass, uuid) match {
      case Some(entity) => entity
      case None => {
        val entity: CqrsEntity = entityClass.newInstance
        entity.uuid = uuid
        entity.version = 0
        entity
      }
    }
  }

  def addListener(entityClass: Class[_ <: CqrsEntity], listener: DataStoreListener) {
    val entityListeners = listeners.getOrElseUpdate(entityClass, new ListBuffer[DataStoreListener])
    entityListeners += listener
  }

  def onNewEvent(event: CqrsEvent) {
    val entity = getEntity(event.entityClass, event.entityUuid)

    val entityListenersOption = listeners.get(event.entityClass)
    
    if(entityListenersOption.isDefined) {
      entityListenersOption.get.foreach(listener => listener.onEntityChanged(entity))
    }
  }
}
