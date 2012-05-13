package pl.marpiec.mailsender

import java.util.Properties
import javax.mail.internet.{InternetAddress, MimeMessage}
import javax.mail._
import pl.marpiec.util.TemplateUtil
import java.lang.Runnable
import org.springframework.stereotype.Service

/**
 * @author Marcin Pieciukiewicz
 */

@Service("mailSender")
class MailSenderImpl extends MailSender {


  def sendMail(subject:String, template:String, address:String, params:Map[String, String]) {
    val task = new SendMailTask(subject, template, address, params)
    new Thread(task).start()
  }
}


class SendMailTask(val subject:String, val template:String, val address:String, val params:Map[String, String]) extends Runnable {

  def run() {
    val props:Properties = new Properties

    props.put("mail.smtp.user", "marpiec.socnet@gmail.com")

    props.put("mail.smtp.host", "smtp.gmail.com")
    props.put("mail.smtp.port", "465")

    props.put("mail.smtp.starttls.enable","true")
//    props.put("mail.smtp.debug", "true");

    props.put("mail.smtp.auth", "true")

    props.put("mail.smtp.socketFactory.port", "465");
    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    props.put("mail.smtp.socketFactory.fallback", "false");

    val session: Session  = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
      override def getPasswordAuthentication:PasswordAuthentication = {
        new PasswordAuthentication("marpiec.socnet@gmail.com", "haslosocnetu");
      }
    });

//    session.setDebug(true)

    try {

      val message:Message = new MimeMessage(session);
      message.setFrom(new InternetAddress("marpiec.socnet@gmail.com"));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(address).asInstanceOf[Array[Address]]);
      message.setSubject(subject);
      message.setContent(TemplateUtil.fillTemplate(template, params), "text/html; charset=UTF-8")
      //message.setText("testowy mail")

      val transport:Transport = session.getTransport("smtps");
      transport.connect ("smtp.gmail.com", 465, "marpiec.socnet@gmail.com", "haslosocnetu");
      transport.sendMessage(message, message.getAllRecipients());
      transport.close();

    } catch {
      case ex: MessagingException => ex.printStackTrace()
    }
  }
}

