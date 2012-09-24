package pl.marpiec.util.mpjson.deserializer

import inner.AbstractFloatingPointDeserializer


/**
 * @author Marcin Pieciukiewicz
 */

object FloatDeserializer extends AbstractFloatingPointDeserializer[Float] {

  protected def toProperFloatingPoint(identifier: StringBuilder) = identifier.toFloat

}
