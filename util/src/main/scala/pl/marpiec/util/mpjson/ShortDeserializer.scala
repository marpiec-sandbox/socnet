package pl.marpiec.util.mpjson

/**
 * @author Marcin Pieciukiewicz
 */

object ShortDeserializer extends AbstractIntegerDeserializer[Short] {

  protected def toProperInteger(identifier: StringBuilder) = identifier.toShort

}
