package pl.marpiec.socnet.web.text

/**
 * @author Marcin Pieciukiewicz
 */

object PolishTextUtil {

  def getInvitationsLinkMessage(count:Int):String = {
    
    if (count == 1) {
      "Masz " + count + " nowe zaproszenie do kontaktu"
    } else if (count >= 2 && count <= 4) {
      "Masz " + count + " nowe zaproszenia do kontaktu"
    } else {
      "Masz " + count + " nowych zaproszeń do kontaktu"
    }
    
  }
  
}
