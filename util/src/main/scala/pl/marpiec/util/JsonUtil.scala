package pl.marpiec.util

import java.lang.reflect.Type
import com.google.gson._
import java.util.Date
import org.joda.time.{Instant, LocalDateTime, DateTime}

/**
 * @author Marcin Pieciukiewicz
 */

class DateTimeTypeConverter extends JsonSerializer[LocalDateTime] with JsonDeserializer[LocalDateTime] {
  def serialize(src: LocalDateTime, typeOfSrc: Type, context: JsonSerializationContext) = {
    new JsonPrimitive(src.toString());
  }

  def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) = {
    try {
      new LocalDateTime(json.getAsString());
    } catch {
      case e:Exception  => {
        // May be it came in formatted as a java.util.Date, so try that
        val date:Date = context.deserialize(json, classOf[Date]);
        new LocalDateTime(date);
      }
    }
  }
}

class InstantTypeConverter extends JsonSerializer[Instant] with JsonDeserializer[Instant] {
  def serialize(src: Instant, typeOfSrc: Type, context: JsonSerializationContext) = {
    new JsonPrimitive(src.getMillis())
  }

  def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) = {
    new Instant(json.getAsLong())
  }
}

class JsonUtil {

  val gson = buildGson
  
  private def buildGson:Gson = {
    val gsonBuilder = new GsonBuilder
    gsonBuilder.registerTypeAdapter(classOf[LocalDateTime], new DateTimeTypeConverter)
    gsonBuilder.registerTypeAdapter(classOf[Instant], new InstantTypeConverter)
    gsonBuilder.create
  }
  
  def toJson(obj:AnyRef):String = {
    gson.toJson(obj)
  }
  
  def fromJson[E](json:String, clazz: Class[E]):E = {
    gson.fromJson(json, clazz)
  }



  
}
