package pl.marpiec.util.mpjson.deserializer

import java.lang.reflect.Field
import pl.marpiec.util.mpjson.{JsonTypeDeserializer, DeserializerFactory, StringIterator}
import pl.marpiec.util.mpjson.util.ScalaLanguageUtils

/**
 * @author Marcin Pieciukiewicz
 */

object BeanDeserializer extends JsonTypeDeserializer[Any] {

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
      val setter = clazz.getDeclaredMethod(ScalaLanguageUtils.getSetterName(identifier), fieldType)

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
