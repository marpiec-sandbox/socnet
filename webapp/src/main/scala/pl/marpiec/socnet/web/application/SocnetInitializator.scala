package pl.marpiec.socnet.web.application

import org.joda.time.LocalDateTime
import org.springframework.stereotype.Service
import pl.marpiec.cqrs.{EventStore, DatabaseInitializer}
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author Marcin Pieciukiewicz
 */

object SocnetInitializator {

  def apply(eventStore:EventStore, databaseInitializer:DatabaseInitializer) {

    databaseInitializer.initDatabase()

    // for faster jodatime initialization (first new LocalDateTime call took about 1 sec, now its about 50 mills)
    System.setProperty("org.joda.time.DateTimeZone.Provider", "org.joda.time.tz.UTCProvider");
    // to initialize jodatime, before time meassurement
    new LocalDateTime()

    val start = System.currentTimeMillis
    print("Starting rebuilding read databases... ")

    eventStore.callListenersForAllAggregates

    val end = System.currentTimeMillis
    println("Done in "+(end-start)+" mills.")

  }
}


