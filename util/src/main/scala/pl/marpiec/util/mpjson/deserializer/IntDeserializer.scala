package pl.marpiec.util.mpjson.deserializer

/**
 * @author Marcin Pieciukiewicz
 */

object IntDeserializer extends AbstractIntegerDeserializer[Int] {

  protected def toProperInteger(identifier: StringBuilder) = identifier.toInt

}
