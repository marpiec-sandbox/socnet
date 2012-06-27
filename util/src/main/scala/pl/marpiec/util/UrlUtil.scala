package pl.marpiec.util

/**
 * @author Marcin Pieciukiewicz
 */

object UrlUtil {
  
  def addHttpIfNoProtocol(url:String):String = {
    if (url.matches("[a-zA-Z]+://.*")) {
      url
    } else {
      "http://" + url
    }
  }
  
  def removeProtocol(url:String):String = {
    url.replaceFirst("^[a-zA-Z]+://", "")
  }

}
