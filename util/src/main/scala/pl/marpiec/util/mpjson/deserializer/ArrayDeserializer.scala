package pl.marpiec.util.mpjson.deserializer

import pl.marpiec.util.mpjson.{DeserializerFactory, StringIterator}
import java.lang.reflect.Field


/**
 * @author Marcin Pieciukiewicz
 */

object ArrayDeserializer extends SimpleValueDeserializer[Array[_]] {

  def deserialize(jsonIterator: StringIterator, clazz: Class[_], field:Field) = {
    jsonIterator.nextChar

    if (jsonIterator.currentChar != '[') {
      throw new IllegalArgumentException("Array should start with '[' symbol but was [" + jsonIterator.currentChar + "], object type is " + clazz)
    }

    var listInstance = List[Any]()

    val elementsType = clazz.getComponentType

    while (jsonIterator.currentChar != ']') {

      val deserializer = DeserializerFactory.getDeserializer(elementsType)
      val value = deserializer.deserialize(jsonIterator, elementsType, field)

      listInstance = value :: listInstance
    }

    jsonIterator.nextChar

    val array: Array[_] = java.lang.reflect.Array.newInstance(elementsType, listInstance.size).asInstanceOf[Array[_]]

    var p = 0
    listInstance = listInstance.reverse
    while (listInstance.nonEmpty) {
      java.lang.reflect.Array.set(array, p, listInstance.head)
      listInstance = listInstance.tail
      p = p + 1
    }

    array
  }

}
