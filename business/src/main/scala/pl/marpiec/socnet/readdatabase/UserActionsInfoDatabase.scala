package pl.marpiec.socnet.readdatabase

import pl.marpiec.util.UID
import pl.marpiec.socnet.model.UserActionsInfo

/**
 * @author Marcin Pieciukiewicz
 */

trait UserActionsInfoDatabase {

  def getUserActionsInfoByUserId(userId: UID): Option[UserActionsInfo]

  def getUserActionsInfoById(id: UID): Option[UserActionsInfo]

}
