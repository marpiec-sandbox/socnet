package pl.marpiec.util.json

import org.joda.time.LocalDateTime
import java.lang.reflect.Type
import com.google.gson._
import java.util.Date

/**
 * @author Marcin Pieciukiewicz
 */

class LocalDateTimeTypeConverter extends JsonSerializer[LocalDateTime] with JsonDeserializer[LocalDateTime] {
  def serialize(src: LocalDateTime, typeOfSrc: Type, context: JsonSerializationContext) = {
    new JsonPrimitive(src.toString("yyyy-MM-dd HH:mm:ss:SSS"));
  }

  def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) = {
    try {
      new LocalDateTime(json.getAsString());
    } catch {
      case e: Exception => {
        // May be it came in formatted as a java.util.Date, so try that
        val date: Date = context.deserialize(json, classOf[Date]);
        new LocalDateTime(date);
      }
    }
  }
}