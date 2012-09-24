package pl.marpiec.util

import mpjson.deserializer.BeanDeserializer
import mpjson.serializer.ObjectSerializer
import mpjson.{StringIterator}


/**
 * @author Marcin Pieciukiewicz
 *
 *
 * TODO:
 * - Serializacja, deserializacja Map
 * - Serializacja, deserializacja typu Class
 * - Serializacja deserializacja pozostałych prymitywów
 * - Zmiana fabryk na dynamiczne
 * - Refaktoryzacja
 * - Dodanie obsługi nietypowych typów
 */

object MPJson {

  def deserialize(json: String, clazz: Class[_]): Any = {
    val jsonIterator = new StringIterator(json)
    BeanDeserializer.deserialize(jsonIterator, clazz, null)
  }

  def serialize(obj: AnyRef): String = {
    val json = new StringBuilder()
    ObjectSerializer.serialize(obj, json)
    json.toString
  }
}
