package pl.marpiec.util.mpjson.deserializer

import java.lang.reflect.{ParameterizedType, Field}
import pl.marpiec.util.mpjson.{DeserializerFactory, StringIterator, JsonTypeDeserializer}
import pl.marpiec.util.json.annotation.{SecondSubType, FirstSubType}


/**
 * @author Marcin Pieciukiewicz
 */

object Tuple2Deserializer extends JsonTypeDeserializer[Tuple2[_,_]] {
  def deserialize(jsonIterator: StringIterator, clazz: Class[_], field: Field):Tuple2[_,_] = {
    jsonIterator.nextChar

    if (jsonIterator.currentChar != '[') {
      throw new IllegalArgumentException("Tuple should start with '[' symbol but was [" + jsonIterator.currentChar + "], object type is " + clazz)
    }

    val types = field.getGenericType.asInstanceOf[ParameterizedType].getActualTypeArguments()

    var firstElementType = types(0).asInstanceOf[Class[_]]
    var secondElementType = types(1).asInstanceOf[Class[_]]

    if(firstElementType.equals(classOf[Object])) {
      val subtype = field.getAnnotation(classOf[FirstSubType])
      if (subtype!=null) {
        firstElementType = subtype.value()
      }
    }

    if(secondElementType.equals(classOf[Object])) {
      val subtype = field.getAnnotation(classOf[SecondSubType])
      if (subtype!=null) {
        secondElementType = subtype.value()
      }
    }

    val first = DeserializerFactory.getDeserializer(firstElementType).deserialize(jsonIterator, firstElementType, field)

    if (jsonIterator.currentChar != ',') {
      throw new IllegalArgumentException("Tuples values should be separated by with ',' symbol but was [" + jsonIterator.currentChar + "], object type is " + clazz)
    }

    val second = DeserializerFactory.getDeserializer(secondElementType).deserialize(jsonIterator, secondElementType, field)

    jsonIterator.nextChar
    (first,second)
  }
}
