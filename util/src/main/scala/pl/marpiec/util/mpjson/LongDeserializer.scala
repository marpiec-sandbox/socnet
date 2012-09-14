package pl.marpiec.util.mpjson

/**
 * @author Marcin Pieciukiewicz
 */

object LongDeserializer extends AbstractIntegerDeserializer[Long] {

  protected def toProperInteger(identifier: StringBuilder) = identifier.toLong

}
