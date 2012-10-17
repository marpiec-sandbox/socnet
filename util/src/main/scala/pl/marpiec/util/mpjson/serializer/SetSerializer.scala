package pl.marpiec.util.mpjson.serializer

import common.IteratorSerializer
import pl.marpiec.util.mpjson.JsonTypeSerializer

object SetSerializer extends JsonTypeSerializer {

  def serialize(obj: Any, jsonBuilder: StringBuilder) = {
    IteratorSerializer.serialize(obj.asInstanceOf[Set[_]].iterator, jsonBuilder)
  }

}
