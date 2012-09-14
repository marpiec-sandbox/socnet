package pl.marpiec.util.mpjson

/**
 * @author Marcin Pieciukiewicz
 */

object LongDeserializer extends SimpleValueDeserializer[Long] {
  def deserialize(jsonIterator: StringIterator, clazz: Class[_]): Long = {

    val identifier = new StringBuilder()

    var currentChar = jsonIterator.getNextChar

    while (currentChar >= '0' && currentChar <= '9' || currentChar == '-') {
      identifier.append(currentChar)
      currentChar = jsonIterator.getNextChar
    }

    identifier.toLong
  }
}
