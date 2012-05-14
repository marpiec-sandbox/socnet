package socnet.service.usercontacts

import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

trait UserContactsCommand {
  def createUserContacts(userId:UID, userAggregateId: UID, newUserContactId:UID)
  def addContact(userId: UID, id:UID, version:Int, contactUserId: UID, contactId: UID)
}
