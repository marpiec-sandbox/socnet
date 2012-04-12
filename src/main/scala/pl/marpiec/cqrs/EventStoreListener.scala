package pl.marpiec.cqrs

import pl.marpiec.socnet.model.User
import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

trait EventStoreListener {
  def startListeningToEventStore(eventStore:EventStore) {
    eventStore.addListener(this)
  }

  def onEntityChanged(entityClass:Class[_ <: CqrsEntity], entityId:UID)
}
