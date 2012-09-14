package pl.marpiec.util.mpjson.deserializer

import pl.marpiec.util.mpjson.StringIterator

/**
 * @author Marcin Pieciukiewicz
 */

trait SimpleValueDeserializer[T] {
  def deserialize(jsonIterator: StringIterator, clazz: Class[_]): T
}
