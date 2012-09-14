package pl.marpiec.util.mpjson

/**
 * @author Marcin Pieciukiewicz
 */

object IntDeserializer extends SimpleValueDeserializer[Int]  {
  def deserialize(jsonIterator: StringIterator, clazz: Class[_]): Int = {

    val identifier = new StringBuilder()

    var currentChar = jsonIterator.getNextChar

    while (currentChar >= '0' && currentChar <= '9' || currentChar == '-') {
      identifier.append(currentChar)
      currentChar = jsonIterator.getNextChar
    }

    identifier.toInt
  }
}
