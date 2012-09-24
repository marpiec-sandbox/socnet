package pl.marpiec.util.mpjson

/**
 * @author Marcin Pieciukiewicz
 */

trait JsonTypeConverter[T] extends JsonTypeDeserializer[T] with JsonTypeSerializer