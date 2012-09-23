package pl.marpiec.util.mpjson.serializer

object SimpleToStringSerializer extends SimpleSerializer {
  def serialize(obj:Any, jsonBuilder:StringBuilder) {
    jsonBuilder.append(obj)
  }
}
