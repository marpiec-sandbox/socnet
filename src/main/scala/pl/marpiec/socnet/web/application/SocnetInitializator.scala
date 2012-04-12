package pl.marpiec.socnet.web.application

import pl.marpiec.socnet.service.user.UserCommand
import pl.marpiec.socnet.di.Factory

/**
 * @author Marcin Pieciukiewicz
 */

object SocnetInitializator {

  val userCommand: UserCommand = Factory.userCommand

  def apply() {

    //userCommand.registerUser("Marcin Pieciukiewicz", "m.pieciukiewicz@socnet", "haslo")


  }
}
