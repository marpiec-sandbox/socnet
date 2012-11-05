package pl.marpiec.socnet.model

import pl.marpiec.cqrs.Aggregate
import pl.marpiec.util.{BeanUtil, UID}
import org.joda.time.LocalDateTime

/**
 * @author Marcin Pieciukiewicz
 */

class UserActionsInfo extends Aggregate(null, 0) {

  var userId: UID = _

  var contactInvitationsReadTimeOption:Option[LocalDateTime] = None
  
  def copy = {
    BeanUtil.copyProperties(new UserActionsInfo, this)
  }
}
