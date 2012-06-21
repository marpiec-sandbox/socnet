package pl.marpiec.socnet.mailtemplate

/**
 * @author Marcin Pieciukiewicz
 */

trait TemplateRepository {

  def getConfirmRegistrationMail: String

  def getChangeForgottenPasswordMail: String

}
