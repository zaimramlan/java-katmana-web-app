package com.katmana;
import javax.servlet.http.HttpServletRequest;

import com.katmana.model.User;
 
// from http://www.mkyong.com/java/javamail-api-sending-email-via-gmail-smtp-example/
import java.util.Properties;
 
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
 
public class Mailer {
 
  public static void mail(User user, String content) {
 
    final String username = "adm.katmana@gmail.com";
    final String password = "4Dmk@tMana";
 
    Properties props = new Properties();
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.socketFactory.port", "465");
    props.put("mail.smtp.socketFactory.class",
        "javax.net.ssl.SSLSocketFactory");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.port", "465");
 
    Session session = Session.getInstance(props,
      new javax.mail.Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
      }
      });
 
    try {
 
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(username));
      message.setRecipients(Message.RecipientType.TO,
        InternetAddress.parse(user.getEmail()));
      message.setSubject("Katmana Password Recovery");
      message.setText("Dear "+user.getName()+","
        + "\n\n Your new email is "+content);
 System.out.println("got here");
      Transport.send(message);
 
      System.out.println("Done");
 
    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }
}