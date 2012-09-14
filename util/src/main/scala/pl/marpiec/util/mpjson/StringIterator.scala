package pl.marpiec.util.mpjson

/**
 * @author Marcin Pieciukiewicz
 */

class StringIterator(val stringValue: String) {

  var nextIndex = 0
  var currentChar: Char = _

  def nextChar = {
    currentChar = stringValue.charAt(nextIndex)
    nextIndex = nextIndex + 1
  }

  def isCurrentCharASmallLetter: Boolean = {
    currentChar >= 'a' && currentChar <= 'z'
  }

  def isCurrentCharADigitPart: Boolean = {
    currentChar >= '0' && currentChar <= '9' || currentChar == '-'
  }

}
