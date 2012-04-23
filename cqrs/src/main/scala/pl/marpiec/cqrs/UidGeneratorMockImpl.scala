package pl.marpiec.cqrs

import pl.marpiec.util.UID

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class UidGeneratorMockImpl extends UidGenerator {

  var uid: Long = 0

  def nextUid = {
    uid = uid + 1
    new UID(uid)
  }
}
