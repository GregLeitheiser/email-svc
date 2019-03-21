package org.servantscode.email;

import org.servantscode.commons.EnvProperty;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

import static javax.mail.Message.RecipientType.CC;
import static javax.mail.Message.RecipientType.TO;
import static javax.mail.internet.InternetAddress.parse;
import static org.servantscode.commons.StringUtils.isSet;

public class Mailer {
    private final Properties config;

    public Mailer(Properties config) { this.config = config; }

    public void sendMail(Mail mail) {
        String emailUser = EnvProperty.get("EMAIL_USER");
        String emailPassword = EnvProperty.get("EMAIL_PASSWORD");

        Session session = Session.getDefaultInstance(config, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailUser, emailPassword);
            }
        });

        try {
            Message message = generateMessage(session, mail);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Could not send email!!", e);
        }
    }

    // ----- Private -----
    private Message generateMessage(Session session, Mail mail) throws MessagingException {
        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress(mail.getFrom()));
        for (String email : mail.getTo())
            message.addRecipients(TO, parse(email));
        for (String email : mail.getCc())
            message.addRecipients(CC, parse(email));

        if(isSet(mail.getReplyTo()))
            message.setReplyTo(parse(mail.getReplyTo()));

        message.setSubject(mail.getSubject());

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(mail.getMessage(), "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);
        return message;
    }
}
