package pl.marpiec.util.mpjson.deserializer

import java.lang.reflect.Field
import pl.marpiec.util.mpjson.{JsonTypeDeserializer, StringIterator}

/**
 * @author Marcin Pieciukiewicz
 */

object BooleanDeserializer extends JsonTypeDeserializer[Boolean] {
  def deserialize(jsonIterator: StringIterator, clazz: Class[_], field:Field): Boolean = {

    val booleanString = new StringBuilder()

    jsonIterator.nextChar

    while (jsonIterator.isCurrentCharASmallLetter) {
      booleanString.append(jsonIterator.currentChar)
      jsonIterator.nextChar
    }

    booleanString.toBoolean
  }

}
