package pl.marpiec.cqrs

import pl.marpiec.util.UID
import org.apache.commons.lang.RandomStringUtils

/**
 * @author Marcin Pieciukiewicz
 */

class TriggeredEventsMockImpl extends TriggeredEvents {

  private val TRIGGER_LENGTH = 128

  var eventsDatabase:Map[String, Event] = Map[String, Event]()
  
  def addNewTriggeredEvent(userId: UID, event: Event):String = {
    val trigger = generateRandomTrigger
    eventsDatabase += trigger -> event
    trigger
  }

  def getEventForTrigger(trigger: String) = {
    eventsDatabase.get(trigger)
  }


  def markEventAsExecuted(trigger: String) {
    //do nothing
  }

  private def generateRandomTrigger():String = {
    return RandomStringUtils.randomAlphanumeric(TRIGGER_LENGTH);
  }
}
