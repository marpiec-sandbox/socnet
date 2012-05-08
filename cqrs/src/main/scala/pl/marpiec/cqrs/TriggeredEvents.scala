package pl.marpiec.cqrs

import pl.marpiec.util.UID
import org.h2.engine.User

/**
 * @author Marcin Pieciukiewicz
 */

trait TriggeredEvents {
  def addNewTriggeredEvent(userId:UID, event:Event):String
  def getEventForTrigger(trigger:String):Option[Event]
  def getUserIdAndEventForTrigger(trigger:String):(Option[(UID, Event)])
  def markEventAsExecuted(trigger:String)
}
