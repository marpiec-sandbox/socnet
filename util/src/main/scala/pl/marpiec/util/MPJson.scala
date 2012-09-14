package pl.marpiec.util

import mpjson.{StringIterator, ObjectDeserializer}


/**
 * @author Marcin Pieciukiewicz
 */

object MPJson {

  def deserialize(json: String, clazz: Class[_]): Any = {

    val jsonIterator = new StringIterator(json)

    
    val obj = ObjectDeserializer.deserialize(jsonIterator, clazz)

    obj
    
  }

}
