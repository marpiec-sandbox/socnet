package pl.marpiec.util.mpjson

/**
 * @author Marcin Pieciukiewicz
 */

class StringIterator(val stringValue: String) {

  val stringLength = stringValue.length
  var nextIndex = 1
  var currentChar: Char = stringValue.charAt(0)

  def skipWhitespaceChars {
    while (currentChar.isWhitespace) {
      nextChar
    }
  }
  
  def nextChar {
    currentChar = stringValue.charAt(nextIndex)
    nextIndex = nextIndex + 1
  }

  def nextNonWhitespaceChar {
    do {
      nextChar
    } while (currentChar.isWhitespace)
  }
  
  def nextCharOrNullIfLast = {
    if (nextIndex < stringValue.length()) {
      nextChar
    } else {
      null
    }
  }

  def isCurrentCharASmallLetter: Boolean = {
    currentChar >= 'a' && currentChar <= 'z'
  }

  def isCurrentCharADigitPart: Boolean = {
    currentChar >= '0' && currentChar <= '9' || currentChar == '-'
  }

  def isCurrentCharAFloatingPointPart: Boolean = {
    currentChar >= '0' && currentChar <= '9' || currentChar == '-' || currentChar == '.'
  }
  
  def checkFutureChar:Char = {
    stringValue.charAt(nextIndex)
  }

  def hasNextChar:Boolean = {
    return nextIndex < stringLength
  }

  def consumeObjectStart = {
    skipWhitespaceChars
    if (currentChar != '{') {
      throw new IllegalArgumentException("Object should start with '{' symbol but was [" + currentChar + "]")
    }
    nextChar
  }

  def consumeObjectEnd = {
    skipWhitespaceChars
    if (currentChar != '}') {
      throw new IllegalArgumentException("Object should end with '{' symbol but was [" + currentChar + "]")
    }
    nextChar
  }

  def consumeFieldValueSeparator = {
    skipWhitespaceChars
    if (currentChar != ':') {
      throw new IllegalArgumentException("Field name and value should be separated by ':' symbol but was [" + currentChar + "]")
    }
    nextChar
  }

  def consumeArrayStart = {
    skipWhitespaceChars
    if (currentChar != '[') {
      throw new IllegalArgumentException("Array should start with '[' symbol but was [" + currentChar + "]")
    }
    nextChar
  }

  def consumeArrayEnd = {
    skipWhitespaceChars
    if (currentChar != ']') {
      throw new IllegalArgumentException("Array should end with ']' symbol but was [" + currentChar + "]")
    }
    nextChar
  }

  def consumeArrayValuesSeparator = {
    skipWhitespaceChars
    if (currentChar != ',') {
      throw new IllegalArgumentException("Array values should be separated by ',' symbol but was [" + currentChar + "]")
    }
    nextChar
  }

  def debugShowLeftString:String = {
    stringValue.substring(nextIndex - 1)
  }

}
