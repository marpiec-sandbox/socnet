package pl.marpiec.util

/**
 * Strings utulity class
 * @author Marcin Pieciukiewicz
 */

object Strings {
  
  def equal(str1:String, str2:String):Boolean = {
    if(str1==null && str2==null) {
      true
    } else if (str1==null) {
      false
    } else {
      str1.equals(str2)
    }
  }
  
}