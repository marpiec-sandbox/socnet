package pl.marpiec.util.mpjsoncustom

import org.joda.time.LocalDateTime
import pl.marpiec.mpjsons.{JsonTypeConverter, StringIterator}
import java.lang.reflect.Field
import org.joda.time.format.DateTimeFormat
import pl.marpiec.mpjsons.impl.deserializer.StringDeserializer

/**
 * @author Marcin Pieciukiewicz
 */

object LocalDateTimeConverter extends JsonTypeConverter[LocalDateTime] {

  val PATTERN = "yyyy-MM-dd HH:mm:ss:SSS"

  def serialize(obj: Any, jsonBuilder: StringBuilder) {
    jsonBuilder.append('"')
    jsonBuilder.append(obj.asInstanceOf[LocalDateTime].toString(PATTERN))
    jsonBuilder.append('"')
  }

  def deserialize(jsonIterator: StringIterator, clazz: Class[_], field: Field): LocalDateTime = {
    val stringValue: String = StringDeserializer.deserialize(jsonIterator, null, null)
    DateTimeFormat.forPattern(PATTERN).parseDateTime(stringValue).toLocalDateTime
  }
}
