package pl.marpiec.util.mpjson.deserializer.primitives

import pl.marpiec.util.mpjson.deserializer.inner.AbstractIntegerDeserializer

/**
 * @author Marcin Pieciukiewicz
 */

object ShortDeserializer extends AbstractIntegerDeserializer[Short] {

  protected def toProperInteger(identifier: StringBuilder) = identifier.toShort

}
