package pl.marpiec.cqrs

import pl.marpiec.util.UID

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class Event(var aggregateId:UID, val expectedVersion:Int, val event:CqrsEvent)