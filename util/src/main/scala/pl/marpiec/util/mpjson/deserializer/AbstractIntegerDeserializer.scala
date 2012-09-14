package pl.marpiec.util.mpjson.deserializer

import pl.marpiec.util.mpjson.StringIterator

/**
 * @author Marcin Pieciukiewicz
 */

trait AbstractIntegerDeserializer[T] extends SimpleValueDeserializer[T] {


  def deserialize(jsonIterator: StringIterator, clazz: Class[_]): T = {

    val identifier = new StringBuilder()

    jsonIterator.nextChar

    while (jsonIterator.isCurrentCharADigitPart) {
      identifier.append(jsonIterator.currentChar)
      jsonIterator.nextChar
    }

    toProperInteger(identifier)
  }

  protected def toProperInteger(identifier: StringBuilder): T


}