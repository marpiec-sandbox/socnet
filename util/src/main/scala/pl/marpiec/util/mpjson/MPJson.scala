package pl.marpiec.util.mpjson

import deserializer.BeanDeserializer
import serializer.BeanSerializer

/**
 * @author Marcin Pieciukiewicz
 */

object MPJson {

  def deserialize(json: String, clazz: Class[_]): Any = {
    val jsonIterator = new StringIterator(json)
    try {
      BeanDeserializer.deserialize(jsonIterator, clazz, null)
    } catch {
      case e:RuntimeException => throw new JsonInnerException("Problem deserializing:\n"+json+"\n"+jsonIterator.debugShowConsumedString, e)
    }
  }

  def serialize(obj: AnyRef): String = {
    val json = new StringBuilder()
    BeanSerializer.serialize(obj, json)
    json.toString
  }

  def registerConverter(clazz: Class[_], converter:JsonTypeConverter[_]) {
    SerializerFactory.registerSerializer(clazz, converter)
    DeserializerFactory.registerDeserializer(clazz, converter)
  }

  def registerSuperclassConverter(clazz: Class[_], converter:JsonTypeConverter[_]) {
    SerializerFactory.registerSuperclassSerializer(clazz, converter)
    DeserializerFactory.registerSuperclassDeserializer(clazz, converter)
  }

}
