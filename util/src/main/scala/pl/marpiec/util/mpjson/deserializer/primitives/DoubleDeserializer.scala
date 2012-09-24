package pl.marpiec.util.mpjson.deserializer.primitives

import pl.marpiec.util.mpjson.deserializer.inner.AbstractFloatingPointDeserializer


/**
 * @author Marcin Pieciukiewicz
 */

object DoubleDeserializer extends AbstractFloatingPointDeserializer[Double] {

  protected def toProperFloatingPoint(identifier: StringBuilder) = identifier.toDouble

}
