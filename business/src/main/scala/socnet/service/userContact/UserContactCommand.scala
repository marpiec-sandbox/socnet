package socnet.service.userContact

import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

trait UserContactCommand {
  def createUserContacts(userId:UID, userAggregateId: UID, newUserContactsId:UID)
  def addContact(userId: UID, id:UID, version:Int, contactUserId: UID, contactId: UID)
}
