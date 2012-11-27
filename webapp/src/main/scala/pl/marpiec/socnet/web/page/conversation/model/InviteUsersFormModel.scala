package pl.marpiec.socnet.web.page.conversation.model

import pl.marpiec.socnet.web.wicket.SecureFormModel

/**
 * @author Marcin Pieciukiewicz
 */

class InviteUsersFormModel extends SecureFormModel {
  var users: String = ""

  /**
   * It parses users String to list of ints, in case of any problem it returns empty list
   */
  def parseUsers:List[Int] = {
    
    try {
      if (users.matches("[0-9\\,]+")) {
        val userList = users.split(",")
        userList.map(_.toInt).toList
      } else {
        List()
      }
    } catch {
      case e:Exception => {
        List()
      }
    }
  }
}