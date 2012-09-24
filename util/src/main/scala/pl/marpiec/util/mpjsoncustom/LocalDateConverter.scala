package pl.marpiec.util.mpjsoncustom

import pl.marpiec.util.mpjson.{JsonTypeConverter, StringIterator}
import java.lang.reflect.Field
import org.joda.time.format.DateTimeFormat
import pl.marpiec.util.mpjson.deserializer.StringDeserializer
import org.joda.time.{LocalDate, LocalDateTime}

/**
 * @author Marcin Pieciukiewicz
 */

object LocalDateConverter extends JsonTypeConverter[LocalDate] {

  val PATTERN = "yyyy-MM-dd"

  def serialize(obj: Any, jsonBuilder: StringBuilder) {
    jsonBuilder.append('"')
    jsonBuilder.append(obj.asInstanceOf[LocalDate].toString(PATTERN))
    jsonBuilder.append('"')
  }

  def deserialize(jsonIterator: StringIterator, clazz: Class[_], field: Field): LocalDate = {
    val stringValue: String = StringDeserializer.deserialize(jsonIterator, null, null)
    DateTimeFormat.forPattern(PATTERN).parseDateTime(stringValue).toLocalDate
  }
}
