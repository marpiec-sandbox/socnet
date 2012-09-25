package pl.marpiec.util.mpjson.serializer

import common.IteratorSerializer
import pl.marpiec.util.mpjson.{JsonTypeSerializer, SerializerFactory}

object ArraySerializer extends JsonTypeSerializer {

  def serialize(obj: Any, jsonBuilder:StringBuilder) = {
    IteratorSerializer.serialize(obj.asInstanceOf[Array[_]].iterator, jsonBuilder)
  }

}
