package pl.marpiec.util.mpjson

/**
 * @author Marcin Pieciukiewicz
 */

object StringDeserializer extends SimpleValueDeserializer[String] {
  def deserialize(jsonIterator: StringIterator, clazz: Class[_]): String = {

    var currentChar = jsonIterator.getNextChar

    if (currentChar != '"') {
      throw new IllegalArgumentException("String value shuld start with \", but was [" + currentChar + "]")
    }

    val stringValue = new StringBuilder()

    currentChar = jsonIterator.getNextChar

    while (currentChar != '"') {

      if (currentChar == '\\') {
        currentChar = jsonIterator.getNextChar

        if (currentChar == '"' || currentChar == '\\') {
          stringValue.append(currentChar)
        } else {
          throw new IllegalArgumentException("Unsupported control character [\\" + currentChar + "]")
        }
      } else {
        stringValue.append(currentChar)
      }

      currentChar = jsonIterator.getNextChar
    }

    currentChar = jsonIterator.getNextChar //to pass closing "

    stringValue.toString
  }

}
