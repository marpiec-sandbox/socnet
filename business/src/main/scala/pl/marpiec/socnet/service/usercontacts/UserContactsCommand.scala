package pl.marpiec.socnet.service.usercontacts

import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

trait UserContactsCommand {

  def createUserContacts(userId: UID, userAggregateId: UID, newUserContactId: UID)

  def markUsersAsContacts(userId: UID, invitationSenderUserId: UID, invitationReceiverUserId: UID)

  def removeContact(userId: UID, firstUserId: UID, secondUserId: UID)
}
