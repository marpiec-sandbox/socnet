package socnet.model.usercontacts

import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

class Invitation(val id:UID, val possibleContactUserId:UID, val message:String)
