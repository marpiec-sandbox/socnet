package pl.marpiec.util.mpjson.serializer

import pl.marpiec.util.mpjson.JsonTypeSerializer

object SimpleToStringSerializer extends JsonTypeSerializer {
  def serialize(obj:Any, jsonBuilder:StringBuilder) {
    jsonBuilder.append(obj)
  }
}
