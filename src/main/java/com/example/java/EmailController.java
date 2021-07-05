/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.java;

import classe.Match;
import java.io.IOException;
import java.util.ArrayList;
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
    public String sendEmail(ArrayList<Match> m,String emailReceiver) throws MessagingException, AddressException, IOException {
        sendmail(m,emailReceiver);
        return "Email sent successfully";
   }   
    
    private void sendmail(ArrayList<Match> listeMatch,String emailReceiver) throws AddressException, MessagingException, IOException {
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

   msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailReceiver));
   String subject = "Alert un match ou plusieurs n'a pas encore été trouver";
   msg.setSubject(subject);
   msg.setSentDate(new Date());


   String message = "Les match(s) suivant n'a pas été trouver dans OPENDOTA API <br> ";
   for(int i=0;i<listeMatch.size();i++){
       String addForMessage = "<p> - "+listeMatch.get(i).getNomTeam1()+" vs "+listeMatch.get(i).getNomTeam2()+" le "+listeMatch.get(i).getDatematch()+"   </p> <br>";
       message = message + addForMessage;
   }
   String addForMessage = "Veillez vous connectez et inserer les vainqueurs manuellement";
   message = message + addForMessage;
   
   MimeBodyPart messageBodyPart = new MimeBodyPart();
   messageBodyPart.setContent(message, "text/html");

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
