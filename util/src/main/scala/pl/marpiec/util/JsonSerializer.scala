package pl.marpiec.util

import mpjson.MPJson
import mpjsoncustom.{SEnumConverter, InstantConverter, LocalDateConverter, LocalDateTimeConverter}
import org.joda.time.{Instant, LocalDate, LocalDateTime}
import senum.SEnum

/**
 * @author Marcin Pieciukiewicz
 */

class JsonSerializer {

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
