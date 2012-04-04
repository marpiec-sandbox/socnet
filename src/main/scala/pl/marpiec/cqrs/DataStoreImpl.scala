package pl.marpiec.cqrs

class DataStoreImpl (val eventStore:EventStore) extends DataStore {

  def getEntity(entityClass:Class[_ <: CqrsEntity], id: Int) = {
    val events = eventStore.getEventsForEntity(entityClass, id)

    val entity = entityClass.newInstance

    events.foreach((event) => {
        event.asInstanceOf[CqrsEvent].applyEvent(entity)
        entity.version = entity.version + 1
      }
    )

    entity

  }
}
