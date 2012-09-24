package pl.marpiec.util.mpjson.deserializer

import inner.AbstractIntegerDeserializer

/**
 * @author Marcin Pieciukiewicz
 */

object ShortDeserializer extends AbstractIntegerDeserializer[Short] {

  protected def toProperInteger(identifier: StringBuilder) = identifier.toShort

}
