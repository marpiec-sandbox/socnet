package pl.marpiec.util.mpjson.deserializer

import inner.AbstractIntegerDeserializer

/**
 * @author Marcin Pieciukiewicz
 */

object LongDeserializer extends AbstractIntegerDeserializer[Long] {

  protected def toProperInteger(identifier: StringBuilder) = identifier.toLong

}
