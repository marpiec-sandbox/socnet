package pl.marpiec.util.mpjson.deserializer

import inner.AbstractFloatingPointDeserializer


/**
 * @author Marcin Pieciukiewicz
 */

object DoubleDeserializer extends AbstractFloatingPointDeserializer[Double] {

  protected def toProperFloatingPoint(identifier: StringBuilder) = identifier.toDouble

}
