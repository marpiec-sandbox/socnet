package pl.marpiec.util.mpjson.deserializer

import pl.marpiec.util.mpjson.{JsonTypeDeserializer, DeserializerFactory, StringIterator}
import java.lang.reflect.Field
import pl.marpiec.util.mpjson.util.TypesUtil

/**
 * @author Marcin Pieciukiewicz
 */

object MapDeserializer extends JsonTypeDeserializer[Map[_, _]] {

  def deserialize(jsonIterator: StringIterator, clazz: Class[_], field: Field): Map[_, _] = {

    jsonIterator.nextChar

    if (jsonIterator.currentChar != '{') {
      throw new IllegalArgumentException("Map should start with '{' symbol but was [" + jsonIterator.currentChar + "], object type is " + clazz)
    }

    val (firstElementType, secondElementType) = TypesUtil.getDoubleSubElementsType(field)
    var map = Map[Any, Any]()

    while (jsonIterator.currentChar != '}') {

      val key: Any = DeserializerFactory.getDeserializer(firstElementType).deserialize(jsonIterator, firstElementType, field)

      if (jsonIterator.currentChar != ':') {
        throw new IllegalArgumentException("After type name there should be ':' separator but was [" + jsonIterator.currentChar + "], field=" + field.getName)
      }

      val value: Any = DeserializerFactory.getDeserializer(secondElementType).deserialize(jsonIterator, secondElementType, field)
      map += key -> value
    }

    jsonIterator.nextChar
    map
  }
}
