package pl.marpiec.util.mpjson.gson

import org.joda.time.Instant
import java.lang.reflect.Type
import com.google.gson._

/**
 * @author Marcin Pieciukiewicz
 */

class InstantTypeConverter extends JsonSerializer[Instant] with JsonDeserializer[Instant] {
  def serialize(src: Instant, typeOfSrc: Type, context: JsonSerializationContext) = {
    new JsonPrimitive(src.getMillis)
  }

  def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) = {
    new Instant(json.getAsLong)
  }
}