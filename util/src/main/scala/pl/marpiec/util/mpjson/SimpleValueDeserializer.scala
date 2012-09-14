package pl.marpiec.util.mpjson

/**
 * @author Marcin Pieciukiewicz
 */

trait SimpleValueDeserializer[T] {
  def deserialize(jsonIterator: StringIterator, clazz: Class[_]):T
}
