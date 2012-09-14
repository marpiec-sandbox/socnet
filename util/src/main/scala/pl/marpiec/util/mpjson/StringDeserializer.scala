package pl.marpiec.util.mpjson

/**
 * @author Marcin Pieciukiewicz
 */

object StringDeserializer extends SimpleValueDeserializer[String] {
  def deserialize(jsonIterator: StringIterator, clazz: Class[_]): String = {

    jsonIterator.nextChar

    if (jsonIterator.currentChar != '"') {
      throw new IllegalArgumentException("String value shuld start with \", but was [" + jsonIterator.currentChar + "]")
    }

    val stringValue = new StringBuilder()

    jsonIterator.nextChar

    while (jsonIterator.currentChar != '"') {

      if (jsonIterator.currentChar == '\\') {
        jsonIterator.nextChar

        if (jsonIterator.currentChar == '"' || jsonIterator.currentChar == '\\') {
          stringValue.append(jsonIterator.currentChar)
        } else {
          throw new IllegalArgumentException("Unsupported control character [\\" + jsonIterator.currentChar + "]")
        }
      } else {
        stringValue.append(jsonIterator.currentChar)
      }

      jsonIterator.nextChar
    }

    jsonIterator.nextChar //to pass closing ", it is " for sure because of previous while

    stringValue.toString
  }

}
