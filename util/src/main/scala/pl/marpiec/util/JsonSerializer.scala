package pl.marpiec.util

import com.google.gson._
import json._
import org.joda.time.{Instant, LocalDate, LocalDateTime}
import senum.SEnum

/**
 * @author Marcin Pieciukiewicz
 */

class JsonSerializer {

  val gson = buildGson

  private def buildGson: Gson = {
    val gsonBuilder = new GsonBuilder
    gsonBuilder.registerTypeAdapter(classOf[LocalDateTime], new LocalDateTimeTypeConverter)
    gsonBuilder.registerTypeAdapter(classOf[LocalDate], new LocalDateTypeConverter)
    gsonBuilder.registerTypeAdapter(classOf[Instant], new InstantTypeConverter)
    gsonBuilder.registerTypeAdapter(classOf[Option[Any]], new OptionSerializer)
    gsonBuilder.registerTypeHierarchyAdapter(classOf[SEnum[Any]], new SEnumTypeConverter)
    gsonBuilder.registerTypeAdapter(classOf[Tuple2[Any,  Any]], new Touple2Converter)
    gsonBuilder.registerTypeAdapter(classOf[List[_]], new ListTypeConverter)
    gsonBuilder.create
  }

  def toJson(obj: AnyRef): String = {
    gson.toJson(obj)
  }

  def fromJson[E](json: String, clazz: Class[E]): E = {
    gson.fromJson(json, clazz)
  }

}
