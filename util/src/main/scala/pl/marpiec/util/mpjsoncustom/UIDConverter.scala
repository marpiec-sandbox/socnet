package pl.marpiec.util.mpjsoncustom

import pl.marpiec.util.UID
import pl.marpiec.util.mpjson.{StringIterator, JsonTypeConverter}
import java.lang.reflect.Field
import pl.marpiec.util.mpjson.deserializer.primitives.LongDeserializer

/**
 * @author Marcin Pieciukiewicz
 */

object UIDConverter extends JsonTypeConverter[UID]{
  
  def serialize(obj: Any, jsonBuilder: StringBuilder) = {
    jsonBuilder.append(obj.asInstanceOf[UID].uid)
  }

  def deserialize(jsonIterator: StringIterator, clazz: Class[_], field: Field):UID = {
    val longValue = LongDeserializer.deserialize(jsonIterator, clazz, field)
    new UID(longValue)
  }
}
