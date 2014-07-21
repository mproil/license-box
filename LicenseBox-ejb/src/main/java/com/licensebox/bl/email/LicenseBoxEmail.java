package com.licensebox.bl.email;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Properties;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * This EJB implements the LicenseBoxEmailLocal Interface and uses the Gmail
 * inbox to send emails.
 * 
 * @author Michael Paltsev & Anna Guzman
 */
@Stateless
public class LicenseBoxEmail implements LicenseBoxEmailLocal {
    
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private final String USERNAME = "licenseboxproject@gmail.com";
    private final String PASSWORD = "lozeapzgrzcosxor";
    //</editor-fold>

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(String toFullName, String subject, String message, String toEmail) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
          });

        try {
            Message msg = new MimeMessage(session);
            InternetAddress toAddress = new InternetAddress(toEmail, toFullName);
            msg.setRecipients(Message.RecipientType.TO, new InternetAddress[] {toAddress});
            msg.setSubject(subject);
            msg.setText(message);

            Transport.send(msg);

        } catch (MessagingException | UnsupportedEncodingException ex) {
            System.err.println(ex.getMessage());
            System.err.println(Arrays.toString(ex.getStackTrace()));
        }
    }
    
}