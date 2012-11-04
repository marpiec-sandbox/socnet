package pl.marpiec.socnet.readdatabase.mock

import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.cqrs.DataStore
import pl.marpiec.socnet.model.ContactInvitation
import pl.marpiec.socnet.readdatabase.ContactInvitationDatabase
import pl.marpiec.util.UID
import org.springframework.stereotype.Repository

/**
 * @author Marcin Pieciukiewicz
 */

@Repository("contactInvitationDatabase")
class ContactInvitationDatabaseMockImpl @Autowired()(dataStore: DataStore)
  extends AbstractDatabase[ContactInvitation](dataStore) with ContactInvitationDatabase {

  startListeningToDataStore(dataStore, classOf[ContactInvitation])

  def getSentInvitations(userId: UID) = getAll.filter(invitation => {invitation.senderUserId == userId && invitation.isSent})

  def getReceivedInvitations(userId: UID) = getAll.filter(invitation => {invitation.receiverUserId == userId && invitation.isSent})

  def getInvitation(firstUserId: UID, secondUserId: UID) =
    getAll.find(invitation => {
      (invitation.senderUserId == firstUserId && invitation.receiverUserId == secondUserId ||
        invitation.receiverUserId == firstUserId && invitation.senderUserId == secondUserId) && invitation.isSent
    })

  def getInvitationsForUsers(firstUserId:UID, secondUsersIds: List[UID]) = {
    var resultMap = Map[UID, ContactInvitation]()
    getAll.foreach(invitation => {
       if(invitation.isSent && invitation.senderUserId == firstUserId && secondUsersIds.contains(invitation.receiverUserId)) {
         resultMap += invitation.receiverUserId -> invitation
       } else if (invitation.isSent && invitation.receiverUserId == firstUserId && secondUsersIds.contains(invitation.senderUserId)) {
         resultMap += invitation.senderUserId -> invitation
       }
    })
    resultMap
  }
}
