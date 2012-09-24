package pl.marpiec.util.mpjson

import deserializer._
import primitives._

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
    } else if (clazz.equals(classOf[Double])) {
      return DoubleDeserializer
    } else if (clazz.equals(classOf[Float])) {
      return FloatDeserializer
    } else if (clazz.equals(classOf[Short])) {
      return ShortDeserializer
    } else if (clazz.equals(classOf[Byte])) {
      return ByteDeserializer
    } else if (clazz.isArray) {
      return ArrayDeserializer
    } else if (clazz.equals(classOf[List[_]])) {
      return ListDeserializer
    } else if (clazz.equals(classOf[Tuple2[_,_]])) {
      return Tuple2Deserializer
    } else if (clazz.equals(classOf[Option[_]])) {
      return OptionDeserializer
    }

    val deserializerOption = additionalDeserializers.get(clazz)

    if(deserializerOption.isDefined) {
      return deserializerOption.get
    } else {
      return BeanDeserializer
    }
  }

}
