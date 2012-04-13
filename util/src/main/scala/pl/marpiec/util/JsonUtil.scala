package pl.marpiec.util

import com.google.gson._
import java.util.Date
import org.joda.time._
import java.lang.reflect.{ParameterizedType, Type}

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

class LocalDateTypeConverter extends JsonSerializer[LocalDate] with JsonDeserializer[LocalDate] {
  def serialize(src: LocalDate, typeOfSrc: Type, context: JsonSerializationContext) = {
    new JsonPrimitive(src.toString("yyyy-MM-dd"));
  }

  def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) = {
    try {
      new LocalDate(json.getAsString());
    } catch {
      case e: Exception => {
        // May be it came in formatted as a java.util.Date, so try that
        val date: Date = context.deserialize(json, classOf[Date]);
        new LocalDate(date);
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


class OptionSerializer extends JsonSerializer[Option[Any]] with JsonDeserializer[Option[Any]] {
  def serialize(src: Option[Any], typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
    if (src.isDefined) {
      context.serialize(src.get);
    } else {
      JsonNull.INSTANCE
    }

  }

  def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) = {
    if (json.isJsonNull) {
      None
    } else {
      val arguments = typeOfT.asInstanceOf[ParameterizedType].getActualTypeArguments
      Option(context.deserialize(json, arguments(0)))
    }
  }
}


class JsonUtil {

  val gson = buildGson

  private def buildGson: Gson = {
    val gsonBuilder = new GsonBuilder
    gsonBuilder.registerTypeAdapter(classOf[LocalDateTime], new LocalDateTimeTypeConverter)
    gsonBuilder.registerTypeAdapter(classOf[LocalDate], new LocalDateTypeConverter)
    gsonBuilder.registerTypeAdapter(classOf[Instant], new InstantTypeConverter)
    gsonBuilder.registerTypeAdapter(classOf[Option[Any]], new OptionSerializer)
    gsonBuilder.create
  }

  def toJson(obj: AnyRef): String = {
    gson.toJson(obj)
  }

  def fromJson[E](json: String, clazz: Class[E]): E = {
    gson.fromJson(json, clazz)
  }


}
