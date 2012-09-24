package pl.marpiec.util.mpjson

import java.lang.reflect.Field

/**
 * @author Marcin Pieciukiewicz
 */

trait JsonTypeDeserializer[T] {
  def deserialize(jsonIterator: StringIterator, clazz: Class[_], field: Field): T
}