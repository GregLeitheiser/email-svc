package org.servantscode.email;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.postgresql.util.Base64;
import org.servantscode.commons.db.ConfigDB;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static java.lang.Boolean.parseBoolean;
import static javax.mail.Message.RecipientType.CC;
import static javax.mail.Message.RecipientType.TO;
import static javax.mail.internet.InternetAddress.parse;
import static org.servantscode.commons.ConfigUtils.decryptConfig;
import static org.servantscode.commons.StringUtils.isEmpty;
import static org.servantscode.commons.StringUtils.isSet;

public class Mailer {
    private static final Logger LOG = LogManager.getLogger(Mailer.class);
    private ConfigDB configDB;

    private Map<String, String> mailConfig;

    public Mailer() {
        this.configDB = new ConfigDB();
        mailConfig = configDB.getConfigurations("mail.smtp");
    }

    public void sendMail(Mail mail) {
        Authenticator auth = null;

        if(parseBoolean(mailConfig.get("mail.smtp.auth"))) {
            Map<String, String> userConfig = configDB.getConfigurations("mail.user");
            String emailUser = userConfig.get("mail.user.account");
            String emailPassword = decryptConfig(userConfig.get("mail.user.password"));

            if(isEmpty(emailUser) && isEmpty(emailPassword))
                throw new RuntimeException("Required auth credentials not configured");

            auth = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailUser, emailPassword);
                }
            };
        }

        Properties smtpConfig = new Properties();
        smtpConfig.putAll(mailConfig);

        Session session = Session.getInstance(smtpConfig, auth);

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

        String sendFrom = mailConfig.get("mail.smtp.sendFromUser");
        if(isSet(sendFrom))
            LOG.debug("Sending from configured address: " + sendFrom);
        InternetAddress from = new InternetAddress(isSet(sendFrom)? sendFrom : mail.getFrom());

        LOG.debug(String.format("Sending mail from: %s (Trying to include personal: %s)", from.toString(), from.getPersonal()));
        message.setFrom(from);
        for (String email : mail.getTo())
            message.addRecipients(TO, parse(email));
        for (String email : mail.getCc())
            message.addRecipients(CC, parse(email));

        if(isSet(mail.getReplyTo())) {
            String replyTo = isSet(sendFrom) ?  sendFrom: mail.getReplyTo();
            message.setReplyTo(parse(replyTo));
        }

        message.setSubject(mail.getSubject());

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(mail.getMessage(), "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        for (Mail.Attachment a: mail.getAttachments()) {
            MimeBodyPart attachment = new MimeBodyPart();
            attachment.setFileName(a.getFileName());
            attachment.setDataHandler(new DataHandler(a.getDataSource()));
            multipart.addBodyPart(attachment);
        }

        message.setContent(multipart);
        return message;
    }
}
