package pl.marpiec.cqrs

import pl.marpiec.socnet.model.User

/**
 * @author Marcin Pieciukiewicz
 */

trait EventStoreListener {
  def startListeningToEventStore(eventStore:EventStore) {
    eventStore.addListener(this)
  }

  def onNewEvent(event:CqrsEvent)
}
