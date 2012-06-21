package pl.marpiec.cqrs

import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

trait EventStoreListener {
  def startListeningToEventStore(eventStore: EventStore) {
    eventStore.addListener(this)
  }

  def onEntityChanged(entityClass: Class[_ <: Aggregate], entityId: UID)
}
