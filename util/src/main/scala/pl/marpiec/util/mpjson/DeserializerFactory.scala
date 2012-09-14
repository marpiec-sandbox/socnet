package pl.marpiec.util.mpjson

/**
 * @author Marcin Pieciukiewicz
 */

object DeserializerFactory {

  def getDeserializer(clazz:Class[_]):SimpleValueDeserializer[_] = {
    if(clazz.equals(classOf[Long])) {
      return LongDeserializer
    } else if(clazz.equals(classOf[Int])) {
      return IntDeserializer
    } else if(clazz.equals(classOf[Boolean])) {
      return BooleanDeserializer
    } else if(clazz.equals(classOf[String])) {
      return StringDeserializer
    }

    throw new IllegalStateException("There's no deserializer for "+clazz)

  }
  
}
