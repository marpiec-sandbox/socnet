package pl.marpiec.util.mpjson.deserializer.primitives

import pl.marpiec.util.mpjson.deserializer.inner.AbstractIntegerDeserializer

/**
 * @author Marcin Pieciukiewicz
 */

object ByteDeserializer extends AbstractIntegerDeserializer[Byte] {

  protected def toProperInteger(identifier: StringBuilder) = identifier.toByte

}
