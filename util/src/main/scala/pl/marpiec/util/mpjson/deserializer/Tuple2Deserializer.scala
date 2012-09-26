package pl.marpiec.util.mpjson.deserializer

import java.lang.reflect.Field
import pl.marpiec.util.mpjson.{DeserializerFactory, StringIterator, JsonTypeDeserializer}
import pl.marpiec.util.mpjson.util.TypesUtil


/**
 * @author Marcin Pieciukiewicz
 */

object Tuple2Deserializer extends JsonTypeDeserializer[Tuple2[_, _]] {
  def deserialize(jsonIterator: StringIterator, clazz: Class[_], field: Field): Tuple2[_, _] = {
    jsonIterator.nextChar

    if (jsonIterator.currentChar != '[') {
      throw new IllegalArgumentException("Tuple should start with '[' symbol but was [" + jsonIterator.currentChar + "], object type is " + clazz)
    }

    val (firstElementType, secondElementType) = TypesUtil.getDoubleSubElementsType(field)

    val first = DeserializerFactory.getDeserializer(firstElementType).deserialize(jsonIterator, firstElementType, field)

    if (jsonIterator.currentChar != ',') {
      throw new IllegalArgumentException("Tuples values should be separated by with ',' symbol but was [" + jsonIterator.currentChar + "], object type is " + clazz)
    }

    val second = DeserializerFactory.getDeserializer(secondElementType).deserialize(jsonIterator, secondElementType, field)

    jsonIterator.nextChar
    (first, second)
  }
}
