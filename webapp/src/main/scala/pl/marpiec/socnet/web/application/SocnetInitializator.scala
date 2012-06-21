package pl.marpiec.socnet.web.application

import org.joda.time.LocalDateTime
import pl.marpiec.cqrs.{EventStore, DatabaseInitializer}

/**
 * @author Marcin Pieciukiewicz
 */

object SocnetInitializator {

  def apply(eventStore: EventStore, databaseInitializer: DatabaseInitializer) {

    migrateEvents(eventStore)

    databaseInitializer.initDatabase()

    // for faster jodatime initialization (first new LocalDateTime call took about 1 sec, now its about 50 mills)
    System.setProperty("org.joda.time.DateTimeZone.Provider", "org.joda.time.tz.UTCProvider");
    // to initialize jodatime, before time meassurement
    new LocalDateTime()

    val start = System.currentTimeMillis
    print("Starting rebuilding read databases... ")

    eventStore.callListenersForAllAggregates

    val end = System.currentTimeMillis
    println("Done in " + (end - start) + " mills.")

  }


  def migrateEvents(eventStore: EventStore) {
    // example
    /* eventStore.getAllEventsByType(classOf[CreateMessageEvent]).foreach((eventRow:EventRow) => {
var event = eventRow.event.asInstanceOf[CreateMessageEvent]
if(event.sentTime==null) {
event = new CreateMessageEvent(event.userId, event.messageText, new LocalDateTime(), event.messageId)
val fixedEventRow = new EventRow(eventRow.userId, eventRow.aggregateId, eventRow.expectedVersion, event)
eventStore.updateEvent(fixedEventRow)
}
})         */
    /*
eventStore.getAllEventsByType(classOf[CreateConversationEvent]).foreach((eventRow:EventRow) => {
  var event = eventRow.event.asInstanceOf[CreateConversationEvent]
  if(event.creationTime==null) {
    event = new CreateConversationEvent(event.creatorUserId, event.title, event.participantsUserIds, new LocalDateTime(),
      event.firstMessageText, event.firstMessageId)
    val fixedEventRow = new EventRow(eventRow.userId, eventRow.aggregateId, eventRow.expectedVersion, event)
    eventStore.updateEvent(fixedEventRow)
  }
})    */
  }

}


