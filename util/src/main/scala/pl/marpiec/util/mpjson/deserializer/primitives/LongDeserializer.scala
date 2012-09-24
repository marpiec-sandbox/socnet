package pl.marpiec.util.mpjson.deserializer.primitives

import pl.marpiec.util.mpjson.deserializer.inner.AbstractIntegerDeserializer

/**
 * @author Marcin Pieciukiewicz
 */

object LongDeserializer extends AbstractIntegerDeserializer[Long] {

  protected def toProperInteger(identifier: StringBuilder) = identifier.toLong

}
