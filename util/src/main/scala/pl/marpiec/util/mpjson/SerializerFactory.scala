package pl.marpiec.util.mpjson

import serializer._

object SerializerFactory {

  var additionalSerializers:Map[Class[_], JsonTypeSerializer] = Map[Class[_], JsonTypeSerializer]()

  def registerSerializer(clazz: Class[_], serializer:JsonTypeSerializer) {
    additionalSerializers += clazz -> serializer
  }

  def getSerializer(obj: Any): JsonTypeSerializer = {

    if (obj.isInstanceOf[Long]) {
      return SimpleToStringSerializer
    } else if (obj.isInstanceOf[Int]) {
      return SimpleToStringSerializer
    } else if (obj.isInstanceOf[Short]) {
      return SimpleToStringSerializer
    } else if (obj.isInstanceOf[Byte]) {
      return SimpleToStringSerializer
    } else if (obj.isInstanceOf[Boolean]) {
      return SimpleToStringSerializer
    } else if (obj.isInstanceOf[String]) {
      return StringSerializer
    }  else if (obj.asInstanceOf[AnyRef].getClass.isArray) {
      return ArraySerializer
    } else if (obj.isInstanceOf[List[_]]) {
      return ListSerializer
    }
    
    val serializerOption = additionalSerializers.get(obj.asInstanceOf[AnyRef].getClass) 

    if(serializerOption.isDefined) {
      return serializerOption.get
    } else {
      return ObjectSerializer
    }

  }
}
