package pl.marpiec.util.mpjson.deserializer

/**
 * @author Marcin Pieciukiewicz
 */

object LongDeserializer extends AbstractIntegerDeserializer[Long] {

  protected def toProperInteger(identifier: StringBuilder) = identifier.toLong

}
