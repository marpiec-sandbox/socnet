package pl.marpiec.util.mpjson.gson

import org.joda.time.LocalDateTime
import java.lang.reflect.Type
import com.google.gson._
import java.util.Date
import org.joda.time.format.DateTimeFormat

/**
 * @author Marcin Pieciukiewicz
 */

class LocalDateTimeTypeConverter extends JsonSerializer[LocalDateTime] with JsonDeserializer[LocalDateTime] {

  val PATTERN = "yyyy-MM-dd HH:mm:ss:SSS"

  def serialize(src: LocalDateTime, typeOfSrc: Type, context: JsonSerializationContext) = {
    new JsonPrimitive(src.toString(PATTERN));
  }

  def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) = {
    try {
      DateTimeFormat.forPattern(PATTERN).parseDateTime(json.getAsString).toLocalDateTime
    } catch {
      case e: Exception => {
        // May be it came in formatted as a java.util.Date, so try that
        val date: Date = context.deserialize(json, classOf[Date]);
        new LocalDateTime(date);
      }
    }
  }
}