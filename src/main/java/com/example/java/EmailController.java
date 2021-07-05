/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.java;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;





/**
 *
 * @author tolot
 */
public class EmailController {
    public String sendEmail() throws MessagingException, AddressException, IOException {
        sendmail();
      return "Email sent successfully";
   }   
    
    private void sendmail() throws AddressException, MessagingException, IOException {
   Properties props = new Properties();
   props.put("mail.smtp.auth", "true");
   props.put("mail.smtp.starttls.enable", "true");
   props.put("mail.smtp.host", "smtp.gmail.com");
   props.put("mail.smtp.port", "587");
   
   Session session = Session.getInstance(props, new javax.mail.Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
         return new PasswordAuthentication("dotabetmailtpt@gmail.com", "WTFtennisman1");
      }
   });
   
   Message msg = new MimeMessage(session);
   msg.setFrom(new InternetAddress("dotabetmailtpt@gmail.com", false));

   msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("tolotriniavo.ralaiarisoa@gmail.com"));
   msg.setSubject("Alert un match n'a pas été trouver");
   msg.setSentDate(new Date());


   MimeBodyPart messageBodyPart = new MimeBodyPart();
   messageBodyPart.setContent("Veillez vous connecter, un match n'a pas été trouver", "text/html");

   Multipart multipart = new MimeMultipart();
   multipart.addBodyPart(messageBodyPart);
   
   /*
   MimeBodyPart attachPart = new MimeBodyPart();
   attachPart.attachFile("/var/tmp/image19.png");
   multipart.addBodyPart(attachPart);
*/
   msg.setContent(multipart);
   Transport.send(msg);   
}
   
    
  
}
