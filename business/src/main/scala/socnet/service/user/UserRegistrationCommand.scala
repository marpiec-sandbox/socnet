package socnet.service.user

import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

trait UserRegistrationCommand {
  def triggerUserRegistrationProcess(trigger:String):UID
}
