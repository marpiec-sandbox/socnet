package socnet.template

import pl.marpiec.util.FileReadUtil

/**
 * @author Marcin Pieciukiewicz
 */

class TemplateRepositoryImpl extends TemplateRepository {

  val CONFIRM_REGISTRATION = "confirmRegistration"
  val CHANGE_FORGOTTEN_PASSWORD = "changeForgottenPassword"
  
  var templatesCache = initTemplatesCache

  private def initTemplatesCache():Map[String, String] = {

    var cache = Map[String, String]()
    cache += CONFIRM_REGISTRATION -> FileReadUtil.readClasspathFile("mail/confirmRegistration.html")
    cache += CHANGE_FORGOTTEN_PASSWORD -> FileReadUtil.readClasspathFile("mail/changeForgottenPassword.html")
    cache
  }

  def getConfirmRegistrationMail:String = templatesCache.get(CONFIRM_REGISTRATION).get

  def getChangeForgottenPasswordMail = templatesCache.get(CHANGE_FORGOTTEN_PASSWORD).get
}
