package pl.marpiec.util.mpjson.deserializer

import pl.marpiec.util.mpjson.StringIterator
import java.lang.reflect.Field

/**
 * @author Marcin Pieciukiewicz
 */

trait SimpleValueDeserializer[T] {
  def deserialize(jsonIterator: StringIterator, clazz: Class[_], field:Field): T
}