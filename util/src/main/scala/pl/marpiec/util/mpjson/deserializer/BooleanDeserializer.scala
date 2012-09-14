package pl.marpiec.util.mpjson.deserializer

import pl.marpiec.util.mpjson.StringIterator
import java.lang.reflect.Field

/**
 * @author Marcin Pieciukiewicz
 */

object BooleanDeserializer extends SimpleValueDeserializer[Boolean] {
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
