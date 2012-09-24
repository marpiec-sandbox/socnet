package pl.marpiec.util.mpjson.deserializer

import pl.marpiec.util.mpjson.{JsonTypeDeserializer, DeserializerFactory, StringIterator}
import pl.marpiec.util.mpjson.util.ScalaLanguageUtils
import java.lang.reflect.{ParameterizedType, Field}
import pl.marpiec.util.json.annotation.{SecondSubType, FirstSubType}

/**
 * @author Marcin Pieciukiewicz
 */

object MapDeserializer extends JsonTypeDeserializer[Map[_,_]] {

  def deserialize(jsonIterator: StringIterator, clazz: Class[_], field: Field): Map[_,_] = {

    jsonIterator.nextChar

    if (jsonIterator.currentChar != '{') {
      throw new IllegalArgumentException("Map should start with '{' symbol but was [" + jsonIterator.currentChar + "], object type is " + clazz)
    }

    var map = Map[Any,  Any]()

    val types = field.getGenericType.asInstanceOf[ParameterizedType].getActualTypeArguments()

    var firstElementType = types(0).asInstanceOf[Class[_]]
    var secondElementType = types(1).asInstanceOf[Class[_]]

    if(firstElementType.equals(classOf[Object])) {
      val subtype = field.getAnnotation(classOf[FirstSubType])
      if (subtype == null) {
        throw new IllegalStateException("No @"+classOf[FirstSubType].getSimpleName+" defined for Tuple first element of field "+field.getName)
      } else {
        firstElementType = subtype.value()
      }
    }

    if(secondElementType.equals(classOf[Object])) {
      val subtype = field.getAnnotation(classOf[SecondSubType])
      if (subtype == null) {
        throw new IllegalStateException("No @"+classOf[SecondSubType].getSimpleName+" defined for Tuple second element type of field "+field.getName)
      } else {
        secondElementType = subtype.value()
      }
    }
    

    while (jsonIterator.currentChar != '}') {

      val key:Any = DeserializerFactory.getDeserializer(firstElementType).deserialize(jsonIterator, firstElementType, field)

      if (jsonIterator.currentChar != ':') {
        throw new IllegalArgumentException("After type name there should be ':' separator but was [" + jsonIterator.currentChar + "], field=" + field.getName)
      }

      val value:Any = DeserializerFactory.getDeserializer(secondElementType).deserialize(jsonIterator, secondElementType, field)
      
      map += key -> value

      /*if (jsonIterator.currentChar == ',') {
        jsonIterator.nextChar
      }  */
    }

    jsonIterator.nextChar

    map
  }
}
