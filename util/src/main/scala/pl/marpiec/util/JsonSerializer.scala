package pl.marpiec.util

import com.google.gson._
import json._
import mpjson.MPJson
import mpjsoncustom.{SEnumConverter, InstantConverter, LocalDateConverter, LocalDateTimeConverter}
import org.joda.time.{Instant, LocalDate, LocalDateTime}
import senum.SEnum

/**
 * @author Marcin Pieciukiewicz
 */

class JsonSerializer {


 /* private def buildGson: Gson = {
    val gsonBuilder = new GsonBuilder
    gsonBuilder.registerTypeAdapter(classOf[LocalDateTime], new LocalDateTimeTypeConverter)
    gsonBuilder.registerTypeAdapter(classOf[LocalDate], new LocalDateTypeConverter)
    gsonBuilder.registerTypeAdapter(classOf[Instant], new InstantTypeConverter)
    gsonBuilder.registerTypeAdapter(classOf[Option[Any]], new OptionSerializer)
    gsonBuilder.registerTypeHierarchyAdapter(classOf[SEnum[Any]], new SEnumTypeConverter)
    gsonBuilder.registerTypeAdapter(classOf[Tuple2[Any,  Any]], new Touple2Converter)
    gsonBuilder.registerTypeAdapter(classOf[List[_]], new ListTypeConverter)
    gsonBuilder.create
  }          */

  MPJson.registerConverter(classOf[LocalDateTime], LocalDateTimeConverter)
  MPJson.registerConverter(classOf[LocalDate], LocalDateConverter)
  MPJson.registerConverter(classOf[Instant], InstantConverter)
  MPJson.registerSuperclassConverter(classOf[SEnum[_]], SEnumConverter)


  def toJson(obj: AnyRef): String = {
    MPJson.serialize(obj)
  }

  def fromJson[E](json: String, clazz: Class[E]): E = {
    MPJson.deserialize(json, clazz).asInstanceOf[E]
  }

}
