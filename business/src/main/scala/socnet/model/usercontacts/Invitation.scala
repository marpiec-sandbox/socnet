package socnet.model.usercontacts

import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

class Invitation(val id:UID, val invitedUserId:UID, val message:String)
