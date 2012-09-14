package pl.marpiec.util.mpjson

/**
 * @author Marcin Pieciukiewicz
 */

class StringIterator(val stringValue:String) {
  
  var nextIndex = 0
  var lastChar:Char = _

  def getNextChar:Char = {
    lastChar = stringValue.charAt(nextIndex)
    nextIndex = nextIndex + 1
    lastChar
  }

}
