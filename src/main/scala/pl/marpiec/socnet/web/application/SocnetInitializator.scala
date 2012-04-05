package pl.marpiec.socnet.web.application

import pl.marpiec.di.Factory
import pl.marpiec.socnet.service.user.UserCommandImpl

/**
 * @author Marcin Pieciukiewicz
 */

object SocnetInitializator {

  val userCommand: UserCommandImpl = Factory.userCommand

  def apply() {

    userCommand.registerUser("Marcin Pieciukiewicz", "m.pieciukiewicz@socnet", "haslo")



  }
}
