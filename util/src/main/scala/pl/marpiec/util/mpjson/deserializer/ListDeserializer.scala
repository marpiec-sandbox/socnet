package pl.marpiec.util.mpjson.deserializer

import java.lang.reflect.{Field, ParameterizedType, Type}
import pl.marpiec.util.json.annotation.FirstSubType
import pl.marpiec.util.mpjson.{JsonTypeDeserializer, DeserializerFactory, StringIterator}

object ListDeserializer extends JsonTypeDeserializer[List[_]] {

  def deserialize(jsonIterator: StringIterator, clazz: Class[_], field:Field) = {
    jsonIterator.nextChar

    if (jsonIterator.currentChar != '[') {
      throw new IllegalArgumentException("List should start with '[' symbol but was [" + jsonIterator.currentChar + "], object type is " + field.getType)
    }

    var listInstance = List[Any]()
    
    if (jsonIterator.checkFutureChar == ']') {
      jsonIterator.nextChar
      jsonIterator.nextChar
      listInstance
    } else {

      var elementsType = field.getGenericType.asInstanceOf[ParameterizedType].getActualTypeArguments()(0).asInstanceOf[Class[_]]

      if(elementsType.equals(classOf[Object])) {
        val subtype = field.getAnnotation(classOf[FirstSubType])
        if (subtype == null) {
          throw new IllegalStateException("No @"+classOf[FirstSubType].getSimpleName+" defined for List type of field "+field.getName)
        } else {
          elementsType = subtype.value()
        }
      }

      while (jsonIterator.currentChar != ']') {
        val deserializer = DeserializerFactory.getDeserializer(elementsType)
        val value = deserializer.deserialize(jsonIterator, elementsType, field)
        listInstance = value :: listInstance
      }

      jsonIterator.nextChar
      listInstance.reverse
    }
  }

}
