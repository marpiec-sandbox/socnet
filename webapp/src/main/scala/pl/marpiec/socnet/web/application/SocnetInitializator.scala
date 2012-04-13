package pl.marpiec.socnet.web.application

import pl.marpiec.socnet.di.Factory
import compat.Platform
import org.joda.time.LocalDateTime

/**
 * @author Marcin Pieciukiewicz
 */

object SocnetInitializator {


  val eventStore = Factory.eventStore

  def apply() {

    eventStore.initDatabaseIfNotExists

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