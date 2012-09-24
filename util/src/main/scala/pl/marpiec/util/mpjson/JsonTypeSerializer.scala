package pl.marpiec.util.mpjson

trait JsonTypeSerializer {
  def serialize(obj: Any, jsonBuilder: StringBuilder)
}
