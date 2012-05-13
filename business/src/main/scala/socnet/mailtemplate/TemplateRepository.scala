package socnet.mailtemplate

/**
 * @author Marcin Pieciukiewicz
 */

trait TemplateRepository {

  def getConfirmRegistrationMail:String
  def getChangeForgottenPasswordMail:String
  
}
