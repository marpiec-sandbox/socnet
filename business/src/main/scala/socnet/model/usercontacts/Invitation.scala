package socnet.model.usercontacts

import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

class Invitation(val id:UID, val possibleContactUserId:UID, val message:String) {

  var accepted:Boolean = false
  var declined:Boolean = false
  var removed:Boolean = false

}
