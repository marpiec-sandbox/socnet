package pl.marpiec.util.mpjson

/**
 * @author Marcin Pieciukiewicz
 */

object IdentifierDeserializer {
  def deserialize(jsonIterator: StringIterator):String = {

    val identifier = new StringBuilder().append(jsonIterator.lastChar)

    var currentChar = jsonIterator.getNextChar

    while (currentChar != ':') {
      identifier.append(currentChar)
      currentChar = jsonIterator.getNextChar
    }

    identifier.toString
  }

}
