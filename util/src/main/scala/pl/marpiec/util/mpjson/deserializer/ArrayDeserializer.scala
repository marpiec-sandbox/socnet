package pl.marpiec.util.mpjson.deserializer

import pl.marpiec.util.mpjson.{DeserializerFactory, StringIterator}
import collection.mutable.ArrayBuilder


/**
 * @author Marcin Pieciukiewicz
 */

object ArrayDeserializer extends SimpleValueDeserializer[Array[_]] {

  def deserialize(jsonIterator: StringIterator, clazz: Class[_]) = {
    jsonIterator.nextChar

    if (jsonIterator.currentChar != '[') {
      throw new IllegalArgumentException("Array should start with '[' symbol but was [" + jsonIterator.currentChar + "], object type is " + clazz)
    }

    var listInstance = List[Any]()

    val elementsType = clazz.getComponentType

    while (jsonIterator.currentChar != ']') {

      val deserializer = DeserializerFactory.getDeserializer(elementsType)
      val value = deserializer.deserialize(jsonIterator, elementsType)

      listInstance = value :: listInstance
    }

    jsonIterator.nextChar


    val array:Array[Any] = java.lang.reflect.Array.newInstance(elementsType, listInstance.size).asInstanceOf[Array[Any]]

    var p = 0
    listInstance = listInstance.reverse
    while(listInstance.nonEmpty) {
      array.update(p, listInstance.head)
      listInstance = listInstance.tail
      p = p + 1
    }

    array
  }

}
