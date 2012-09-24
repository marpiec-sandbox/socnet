package pl.marpiec.util.mpjson

import deserializer._
import serializer.ObjectSerializer


/**
 * @author Marcin Pieciukiewicz
 */

object DeserializerFactory {

  var additionalDeserializers:Map[Class[_], JsonTypeDeserializer[_]] = Map[Class[_], JsonTypeDeserializer[_]]()

  def registerDeserializer(clazz: Class[_], deserializer:JsonTypeDeserializer[_]) {
    additionalDeserializers += clazz -> deserializer
  }

  def getDeserializer(clazz: Class[_]): JsonTypeDeserializer[_] = {
    if (clazz.equals(classOf[Long])) {
      return LongDeserializer
    } else if (clazz.equals(classOf[Int])) {
      return IntDeserializer
    } else if (clazz.equals(classOf[Boolean])) {
      return BooleanDeserializer
    } else if (clazz.equals(classOf[String])) {
      return StringDeserializer
    } else if (clazz.equals(classOf[Short])) {
      return ShortDeserializer
    } else if (clazz.equals(classOf[Byte])) {
      return ByteDeserializer
    } else if (clazz.isArray) {
      return ArrayDeserializer
    } else if (clazz.equals(classOf[List[_]])) {
      return ListDeserializer
    }

    val deserializerOption = additionalDeserializers.get(clazz)

    if(deserializerOption.isDefined) {
      return deserializerOption.get
    } else {
      return ObjectDeserializer
    }
  }

}
