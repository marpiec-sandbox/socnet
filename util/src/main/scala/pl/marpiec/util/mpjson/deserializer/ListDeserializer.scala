package pl.marpiec.util.mpjson.deserializer

import pl.marpiec.util.mpjson.{DeserializerFactory, StringIterator}
import java.lang.reflect.{Field, ParameterizedType, Type}
import pl.marpiec.util.json.annotation.SubType

object ListDeserializer extends SimpleValueDeserializer[List[_]] {

  def deserialize(jsonIterator: StringIterator, clazz: Class[_], field:Field) = {
    jsonIterator.nextChar

    if (jsonIterator.currentChar != '[') {
      throw new IllegalArgumentException("List should start with '[' symbol but was [" + jsonIterator.currentChar + "], object type is " + field.getType)
    }

    var listInstance = List[Any]()

    var elementsType = field.getGenericType.asInstanceOf[ParameterizedType].getActualTypeArguments()(0).asInstanceOf[Class[_]]

    if(elementsType.equals(classOf[Object])) {
      val subtype = field.getAnnotation(classOf[SubType])
      if (subtype!=null) {
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
