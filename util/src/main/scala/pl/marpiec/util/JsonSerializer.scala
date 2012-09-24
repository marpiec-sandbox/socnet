package pl.marpiec.util

import com.google.gson._
import json._
import org.joda.time.{Instant, LocalDate, LocalDateTime}
import senum.SEnum

/**
 * @author Marcin Pieciukiewicz
 */

class JsonSerializer {



  def toJson(obj: AnyRef): String = {
    MPJson.serialize(obj)
  }

  def fromJson[E](json: String, clazz: Class[E]): E = {
    MPJson.deserialize(json, clazz).asInstanceOf[E]
  }

}
