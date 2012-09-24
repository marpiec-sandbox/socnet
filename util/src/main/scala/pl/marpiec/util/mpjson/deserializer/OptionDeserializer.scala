package pl.marpiec.util.mpjson.deserializer

import java.lang.reflect.{ParameterizedType, Field}
import pl.marpiec.util.mpjson.{DeserializerFactory, StringIterator, JsonTypeDeserializer}
import pl.marpiec.util.json.annotation.FirstSubType


/**
 * @author Marcin Pieciukiewicz
 */

object OptionDeserializer extends JsonTypeDeserializer[Option[_]] {
  def deserialize(jsonIterator: StringIterator, clazz: Class[_], field: Field): Option[_] = {
    jsonIterator.nextChar

    if (jsonIterator.currentChar != '[') {
      throw new IllegalArgumentException("Option should start with '[' symbol but was [" + jsonIterator.currentChar + "], object type is " + clazz)
    }

    if (jsonIterator.checkFutureChar == ']' ) {
      jsonIterator.nextChar
      jsonIterator.nextChar
      None
    } else {
      val types = field.getGenericType.asInstanceOf[ParameterizedType].getActualTypeArguments()

      var elementType = types(0).asInstanceOf[Class[_]]

      if (elementType.equals(classOf[Object])) {
        val subtype = field.getAnnotation(classOf[FirstSubType])
        if (subtype == null) {
          throw new IllegalStateException("No @"+classOf[FirstSubType].getSimpleName+" defined for Option type of field "+field.getName)
        } else {
          elementType = subtype.value()
        }
      }
      val value = DeserializerFactory.getDeserializer(elementType).deserialize(jsonIterator, elementType, field)

      jsonIterator.nextChar
      Option(value)
    }
  }
}
