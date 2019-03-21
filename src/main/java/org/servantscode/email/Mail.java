package org.servantscode.email;

import java.util.List;

import static java.util.Collections.emptyList;

public class Mail {
    public String from;
    public List<String> to = emptyList();
    public List<String> cc = emptyList();

    public String replyTo;

    public String subject;
    public String message;

    public Mail() {}

    //----- Accessors -----
    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public List<String> getTo() { return to; }
    public void setTo(List<String> to) { this.to = to; }

    public List<String> getCc() { return cc; }
    public void setCc(List<String> cc) { this.cc = cc; }

    public String getReplyTo() { return replyTo; }
    public void setReplyTo(String replyTo) { this.replyTo = replyTo; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
