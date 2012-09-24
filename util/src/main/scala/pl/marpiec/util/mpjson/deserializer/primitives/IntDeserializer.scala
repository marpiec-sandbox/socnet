package pl.marpiec.util.mpjson.deserializer.primitives

import pl.marpiec.util.mpjson.deserializer.inner.AbstractIntegerDeserializer

/**
 * @author Marcin Pieciukiewicz
 */

object IntDeserializer extends AbstractIntegerDeserializer[Int] {

  protected def toProperInteger(identifier: StringBuilder) = identifier.toInt

}
