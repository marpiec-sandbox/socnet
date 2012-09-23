package pl.marpiec.util.mpjson

import deserializer.{ListDeserializer, ArrayDeserializer}
import serializer._

object SerializerFactory {
  def getDeserializer(obj: Any): SimpleSerializer = {

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

    return ObjectSerializer


  }
}
