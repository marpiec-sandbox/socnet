package pl.marpiec.util.mpjson

/**
 * @author Marcin Pieciukiewicz
 */

object BooleanDeserializer extends SimpleValueDeserializer[Boolean] {
  def deserialize(jsonIterator: StringIterator, clazz: Class[_]): Boolean = {

    val booleanString = new StringBuilder()

    var currentChar = jsonIterator.getNextChar

    while (currentChar >= 'a' && currentChar <= 'z') {
      booleanString.append(currentChar)
      currentChar = jsonIterator.getNextChar
    }

    booleanString.toBoolean
  }

}
