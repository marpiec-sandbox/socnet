package pl.marpiec.util.mpjson.serializer


trait SimpleSerializer {
  def serialize(obj:Any, jsonBuilder:StringBuilder)
}
