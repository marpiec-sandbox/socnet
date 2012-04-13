package pl.marpiec.socnet.web.application

import pl.marpiec.socnet.di.Factory
import compat.Platform

/**
 * @author Marcin Pieciukiewicz
 */

object SocnetInitializator {


  val eventStore = Factory.eventStore

  def apply() {

    eventStore.initDatabaseIfNotExists

    val start = System.currentTimeMillis
    print("Starting rebuilding read databases... ")

    eventStore.callListenersForAllAggregates

    val end = System.currentTimeMillis
    println("Done in "+(end-start)+" mills.")

  }
}
