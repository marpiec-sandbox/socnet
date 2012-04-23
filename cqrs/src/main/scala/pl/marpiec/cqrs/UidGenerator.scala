package pl.marpiec.cqrs

import pl.marpiec.util.UID

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

trait UidGenerator {

  def nextUid: UID

}