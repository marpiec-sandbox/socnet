package pl.marpiec.util.mpjson.serializer

import pl.marpiec.util.mpjson.{JsonTypeSerializer, StringIterator}


object StringSerializer extends JsonTypeSerializer {
  def serialize(obj:Any, jsonBuilder:StringBuilder) {

    val iterator = new StringIterator(obj.asInstanceOf[String])

    jsonBuilder.append('"')

    while(iterator.hasNextChar) {
      iterator.nextChar
       val char = iterator.currentChar

      char match {
        case '"' => jsonBuilder.append("\\\"")
        case '\\' => jsonBuilder.append("\\\\")
        case '/' => jsonBuilder.append("\\/")
        case '\b' => jsonBuilder.append("\\b")
        case '\f' => jsonBuilder.append("\\f")
        case '\n' => jsonBuilder.append("\\n")
        case '\r' => jsonBuilder.append("\\r")
        case '\t' => jsonBuilder.append("\\t")
        case _ => jsonBuilder.append(char)
      }
    }

    jsonBuilder.append('"')
  }
}
