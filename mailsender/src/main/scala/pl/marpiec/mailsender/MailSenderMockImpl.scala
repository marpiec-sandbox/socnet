package pl.marpiec.mailsender

/**
 * @author Marcin Pieciukiewicz
 */

class MailSenderMockImpl extends MailSender {
  def sendMail(subject: String, template: String, address: String, params: Map[String, String]) {
    //do nothing
  }
}
