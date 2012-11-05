package pl.marpiec.socnet.service.useractionsinfo

import pl.marpiec.util.UID
import org.joda.time.LocalDateTime

/**
 * @author Marcin Pieciukiewicz
 */

trait UserActionsInfoCommand {

  def createUserActionsInfo(userId: UID, actionsOwnerUserId: UID, newUserActionsInfoId: UID)
  
  def changeContactInvitationsReadTime(userId:UID, id: UID, readTime: LocalDateTime)
  
}
