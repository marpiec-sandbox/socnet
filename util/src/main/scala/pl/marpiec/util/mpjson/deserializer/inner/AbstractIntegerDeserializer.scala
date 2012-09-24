package pl.marpiec.util.mpjson.deserializer.inner

import java.lang.reflect.Field
import pl.marpiec.util.mpjson.{JsonTypeDeserializer, StringIterator}

/**
 * @author Marcin Pieciukiewicz
 */

trait AbstractIntegerDeserializer[T] extends JsonTypeDeserializer[T] {

  def deserialize(jsonIterator: StringIterator, clazz: Class[_], field: Field): T = {

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