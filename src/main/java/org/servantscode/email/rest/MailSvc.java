package org.servantscode.email.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.servantscode.commons.rest.SCServiceBase;
import org.servantscode.email.Mail;
import org.servantscode.email.Mailer;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/email")
public class MailSvc extends SCServiceBase {
    private static final Logger LOG = LogManager.getLogger(MailSvc.class);

    @POST @Consumes(MediaType.APPLICATION_JSON)
    public void sendMail(Mail mail) {
        verifyUserAccess("email.send");

        try {
            new Mailer().sendMail(mail);
            LOG.info("Email sent to: " + (mail.getTo().size() == 1? mail.getTo().get(0): mail.getTo().size() + " recipients."));
        } catch (Throwable t) {
            LOG.error("Failed to send email.", t);
            throw new WebApplicationException("Failed to send email.", t);
        }
    }
}
