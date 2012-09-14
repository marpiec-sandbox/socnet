package pl.marpiec.util.mpjson

/**
 * @author Marcin Pieciukiewicz
 */

object IdentifierDeserializer {
  def deserialize(jsonIterator: StringIterator): String = {

    val identifier = new StringBuilder().append(jsonIterator.currentChar)

    jsonIterator.nextChar

    while (jsonIterator.currentChar != ':') {
      identifier.append(jsonIterator.currentChar)
      jsonIterator.nextChar
    }

    identifier.toString
  }

}
