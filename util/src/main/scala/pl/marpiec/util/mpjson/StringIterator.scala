package pl.marpiec.util.mpjson

/**
 * @author Marcin Pieciukiewicz
 */

class StringIterator(val stringValue: String) {

  val stringLength = stringValue.length
  var nextIndex = 0
  var currentChar: Char = _

  def nextChar = {
    currentChar = stringValue.charAt(nextIndex)
    nextIndex = nextIndex + 1
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

}
