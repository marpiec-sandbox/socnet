package pl.marpiec.util.mpjson.deserializer.primitives

import pl.marpiec.util.mpjson.deserializer.inner.AbstractFloatingPointDeserializer


/**
 * @author Marcin Pieciukiewicz
 */

object FloatDeserializer extends AbstractFloatingPointDeserializer[Float] {

  protected def toProperFloatingPoint(identifier: StringBuilder) = identifier.toFloat

}
