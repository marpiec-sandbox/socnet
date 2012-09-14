package pl.marpiec.util.mpjson

/**
 * @author Marcin Pieciukiewicz
 */

object ObjectDeserializer extends SimpleValueDeserializer[Any] {

  def deserialize(jsonIterator: StringIterator, clazz: Class[_]):Any = {

    var currentChar = jsonIterator.getNextChar
    
    if (currentChar!='{') {
       throw new IllegalArgumentException("Object should start with '{' symbol but was ["+currentChar+"], object type is "+clazz)
    }

    var instance: Any = clazz.newInstance()

    currentChar = jsonIterator.getNextChar

    while (currentChar != '}') {

      val identifier = IdentifierDeserializer.deserialize(jsonIterator)

      val field = clazz.getDeclaredField(identifier)
      val fieldType = field.getType
      val setter = clazz.getDeclaredMethod(identifier + "_$eq", fieldType)

      if (jsonIterator.lastChar != ':') {
        throw new IllegalArgumentException("After type name there should be ':' separator but was [" + currentChar + "], field=" + identifier)
      }

      val deserializer = DeserializerFactory.getDeserializer(fieldType)
      
      val value = deserializer.deserialize(jsonIterator, fieldType)

      setter.invoke(instance, value.asInstanceOf[AnyRef])

      currentChar = jsonIterator.lastChar
      if (currentChar == ',') {
        currentChar = jsonIterator.getNextChar
      }
    }

    instance
  }
}
