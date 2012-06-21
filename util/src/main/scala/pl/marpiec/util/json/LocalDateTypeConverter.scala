package pl.marpiec.util.json

import org.joda.time.LocalDate
import java.lang.reflect.Type
import com.google.gson._
import java.util.Date
import org.joda.time.format.DateTimeFormat

/**
 * @author Marcin Pieciukiewicz
 */

class LocalDateTypeConverter extends JsonSerializer[LocalDate] with JsonDeserializer[LocalDate] {

  val PATTERN = "yyyy-MM-dd"

  def serialize(src: LocalDate, typeOfSrc: Type, context: JsonSerializationContext) = {
    new JsonPrimitive(src.toString(PATTERN));
  }

  def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) = {
    try {
      DateTimeFormat.forPattern(PATTERN).parseDateTime(json.getAsString).toLocalDate
    } catch {
      case e: Exception => {
        // May be it came in formatted as a java.util.Date, so try that
        val date: Date = context.deserialize(json, classOf[Date]);
        new LocalDate(date);
      }
    }
  }
}