package pl.marpiec.util

import mpjson.deserializer.ObjectDeserializer
import mpjson.{StringIterator}


/**
 * @author Marcin Pieciukiewicz
 */

object MPJson {

  def deserialize(json: String, clazz: Class[_]): Any = {
    val jsonIterator = new StringIterator(json)
    ObjectDeserializer.deserialize(jsonIterator, clazz)
  }
}
