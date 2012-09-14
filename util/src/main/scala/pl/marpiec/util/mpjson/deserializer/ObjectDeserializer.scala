package pl.marpiec.util.mpjson.deserializer

import pl.marpiec.util.mpjson.{DeserializerFactory, StringIterator}
import java.lang.reflect.Field

/**
 * @author Marcin Pieciukiewicz
 */

object ObjectDeserializer extends SimpleValueDeserializer[Any] {

  def deserialize(jsonIterator: StringIterator, clazz: Class[_], field:Field): Any = {

    jsonIterator.nextChar

    if (jsonIterator.currentChar != '{') {
      throw new IllegalArgumentException("Object should start with '{' symbol but was [" + jsonIterator.currentChar + "], object type is " + clazz)
    }

    val instance: Any = clazz.newInstance()

    jsonIterator.nextChar

    while (jsonIterator.currentChar != '}') {

      val identifier = IdentifierDeserializer.deserialize(jsonIterator)

      val field = clazz.getDeclaredField(identifier)
      val fieldType = field.getType
      val setter = clazz.getDeclaredMethod(identifier + "_$eq", fieldType)

      if (jsonIterator.currentChar != ':') {
        throw new IllegalArgumentException("After type name there should be ':' separator but was [" + jsonIterator.currentChar + "], field=" + identifier)
      }

      val deserializer = DeserializerFactory.getDeserializer(fieldType)

      val value = deserializer.deserialize(jsonIterator, fieldType, field)

      setter.invoke(instance, value.asInstanceOf[AnyRef])

      if (jsonIterator.currentChar == ',') {
        jsonIterator.nextChar
      }
    }

    jsonIterator.nextCharOrNullIfLast

    instance
  }
}
