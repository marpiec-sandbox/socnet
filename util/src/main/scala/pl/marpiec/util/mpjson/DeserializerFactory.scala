package pl.marpiec.util.mpjson

import deserializer._


/**
 * @author Marcin Pieciukiewicz
 */

object DeserializerFactory {

  def getDeserializer(clazz: Class[_]): SimpleValueDeserializer[_] = {
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

    ObjectDeserializer

  }

}
