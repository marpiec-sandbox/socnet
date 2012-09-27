package pl.marpiec.util.mpjson.deserializer

import pl.marpiec.util.mpjson.{JsonTypeDeserializer, DeserializerFactory, StringIterator}
import java.lang.reflect.Field
import pl.marpiec.util.mpjson.util.TypesUtil

/**
 * @author Marcin Pieciukiewicz
 */

object MapDeserializer extends JsonTypeDeserializer[Map[_, _]] {

  def deserialize(jsonIterator: StringIterator, clazz: Class[_], field: Field): Map[_, _] = {

    jsonIterator.consumeArrayStart

    val (firstElementType, secondElementType) = TypesUtil.getDoubleSubElementsType(field)
    var map = Map[Any, Any]()

    jsonIterator.skipWhitespaceChars

    while (jsonIterator.currentChar != ']') {

      jsonIterator.consumeObjectStart

      jsonIterator.skipWhitespaceChars
      jsonIterator.nextNonWhitespaceChar // pass k:
      jsonIterator.nextNonWhitespaceChar

      val key: Any = DeserializerFactory.getDeserializer(firstElementType).deserialize(jsonIterator, firstElementType, field)

      jsonIterator.consumeArrayValuesSeparator

      jsonIterator.skipWhitespaceChars
      jsonIterator.nextNonWhitespaceChar // pass v:
      jsonIterator.nextNonWhitespaceChar

      val value: Any = DeserializerFactory.getDeserializer(secondElementType).deserialize(jsonIterator, secondElementType, field)
      map += key -> value

      jsonIterator.consumeObjectEnd // }
      jsonIterator.skipWhitespaceChars

      if (jsonIterator.currentChar == ',') {
        jsonIterator.nextChar
      }

    }

    jsonIterator.nextChar
    map
  }
}
