package pl.marpiec.cqrs

import pl.marpiec.util.UID

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class EventRow(var userId:UID, var aggregateId:UID, var expectedVersion:Int, val event:Event)
