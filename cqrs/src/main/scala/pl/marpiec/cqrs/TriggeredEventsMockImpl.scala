package pl.marpiec.cqrs

import pl.marpiec.util.UID
import org.apache.commons.lang.RandomStringUtils

/**
 * @author Marcin Pieciukiewicz
 */

class TriggeredEventsMockImpl extends TriggeredEvents {

  private val TRIGGER_LENGTH = 128

  var eventsDatabase:Map[String, (UID, Event)] = Map[String, (UID, Event)]()

  def addNewTriggeredEvent(userId: UID, event: Event):String = {
    val trigger = generateRandomTrigger
    eventsDatabase += trigger -> (userId, event)
    trigger
  }

  def getEventForTrigger(trigger: String) = {
    val resultOption = eventsDatabase.get(trigger)
    if (resultOption.isDefined) {
      Option(resultOption.get._2)
    } else {
      None
    }
  }

  def getUserIdAndEventForTrigger(trigger: String) = {
    eventsDatabase.get(trigger)
  }

  def markEventAsExecuted(trigger: String) {
    //do nothing
  }

  private def generateRandomTrigger():String = {
    return RandomStringUtils.randomAlphanumeric(TRIGGER_LENGTH);
  }


}
