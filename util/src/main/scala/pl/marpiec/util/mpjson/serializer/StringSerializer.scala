package pl.marpiec.util.mpjson.serializer

import pl.marpiec.util.mpjson.StringIterator

object StringSerializer extends SimpleSerializer {
  def serialize(obj:Any, jsonBuilder:StringBuilder) {

    val iterator = new StringIterator(obj.asInstanceOf[String])

    jsonBuilder.append('"')

    while(iterator.hasNextChar) {
      iterator.nextChar
       val char = iterator.currentChar

      if (char=='"') {
        jsonBuilder.append("\\\"")
      } else if (char=='\\') {
        jsonBuilder.append("\\\\")
      } else {
        jsonBuilder.append(char)
      }
    }

    jsonBuilder.append('"')
  }
}
