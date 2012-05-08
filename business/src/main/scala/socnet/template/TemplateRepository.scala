package socnet.template

/**
 * @author Marcin Pieciukiewicz
 */

trait TemplateRepository {

  def getConfirmRegistrationMail:String
  def getChangeForgottenPasswordMail:String
  
}
