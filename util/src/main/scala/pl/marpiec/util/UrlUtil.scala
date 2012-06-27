package pl.marpiec.util

/**
 * @author Marcin Pieciukiewicz
 */

object UrlUtil {
  
  def addHttpIfNoProtocol(url:String):String = {
    if (url == null || url.matches("[a-zA-Z]+://.*")) {
      url
    } else {
      "http://" + url
    }
  }
  
  def removeProtocol(url:String):String = {
    if(url == null) {
      null
    } else {
      url.replaceFirst("^[a-zA-Z]+://", "")
    }
    
  }

}
