package pl.marpiec.mailsender

import java.util.Properties
import javax.mail.internet.{InternetAddress, MimeMessage}
import javax.mail._
import pl.marpiec.util.TemplateUtil

/**
 * @author Marcin Pieciukiewicz
 */

class SendMailTask(val subject:String, val template:String, val address:String, val params:Map[String, String]) extends Runnable {

  def run() {
    val props:Properties = new Properties
    props.put("mail.smtp.auth", "true")
    props.put("mail.smtp.starttls.enable", "true")
    props.put("mail.smtp.host", "smtp.gmail.com")
    props.put("mail.smtp.port", "587")


    val session: Session  = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
      override def getPasswordAuthentication:PasswordAuthentication = {
        new PasswordAuthentication("marpiec.socnet", "haslosocnetu");
      }
    });

    try {

      val message:Message = new MimeMessage(session);
      message.setFrom(new InternetAddress("marpiec.socnet@gmail.com"));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(address).asInstanceOf[Array[Address]]);
      message.setSubject(subject);
      message.setText(TemplateUtil.fillTemplate(template, params));

      Transport.send(message);

    } catch {
      case ex: MessagingException => ex.printStackTrace()
    }
  }
}

class MailSenderImpl extends MailSender {


  def sendMail(subject:String, template:String, address:String, params:Map[String, String]) {
    val task = new SendMailTask(subject, template, address, params)
    new Thread(task).start()
  }
}
