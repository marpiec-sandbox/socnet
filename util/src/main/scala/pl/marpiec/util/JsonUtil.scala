package pl.marpiec.util

import com.google.gson._
import json.{OptionSerializer, InstantTypeConverter, LocalDateTypeConverter, LocalDateTimeTypeConverter}
import org.joda.time.{Instant, LocalDate, LocalDateTime}

/**
 * @author Marcin Pieciukiewicz
 */

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
