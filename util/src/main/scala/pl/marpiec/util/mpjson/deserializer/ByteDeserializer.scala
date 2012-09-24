package pl.marpiec.util.mpjson.deserializer

import inner.AbstractIntegerDeserializer

/**
 * @author Marcin Pieciukiewicz
 */

object ByteDeserializer extends AbstractIntegerDeserializer[Byte] {

  protected def toProperInteger(identifier: StringBuilder) = identifier.toByte

}
