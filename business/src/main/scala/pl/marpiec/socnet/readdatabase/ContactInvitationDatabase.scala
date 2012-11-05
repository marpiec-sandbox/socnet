package pl.marpiec.socnet.readdatabase

import pl.marpiec.util.UID
import pl.marpiec.socnet.model.ContactInvitation
import org.joda.time.LocalDateTime

/**
 * @author Marcin Pieciukiewicz
 */
trait ContactInvitationDatabase {

  def getSentInvitations(userId: UID):List[ContactInvitation]
  def getReceivedInvitations(userId: UID):List[ContactInvitation]
  def getAllInvitations(userId:UID):List[ContactInvitation]
  def getInvitation(firstUserId: UID, secondUserId:UID):Option[ContactInvitation]
  def getInvitationsForUsers(firstUserId:UID, secondUsersIds: List[UID]):Map[UID, ContactInvitation]
  def getInvitationsCount(userId: UID, invitationsNewerThanTimeOption: Option[LocalDateTime]):Int

}
