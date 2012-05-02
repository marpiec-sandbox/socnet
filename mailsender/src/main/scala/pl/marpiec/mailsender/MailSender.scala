package pl.marpiec.mailsender

/**
 * @author Marcin Pieciukiewicz
 */

trait MailSender {
  def sendMail(subject:String, template:String, address:String, params:Map[String, String])
}
