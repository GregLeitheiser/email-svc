package org.servantscode.email;

import org.postgresql.util.Base64;

import javax.activation.DataSource;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import static java.util.Collections.emptyList;

public class Mail {
    public String from;
    public List<String> to = emptyList();
    public List<String> cc = emptyList();

    public String replyTo;

    public String subject;
    public String message;

    public List<Attachment> attachments;

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

    public List<Attachment> getAttachments() { return attachments; }
    public void setAttachments(List<Attachment> attachments) { this.attachments = attachments; }

    public static class Attachment {
        public String fileName;
        public String mimeType;
        public String data;

        public String getFileName() { return fileName; }
        public void setFileName(String fileName) { this.fileName = fileName; }

        public String getMimeType() { return mimeType; }
        public void setMimeType(String mimeType) { this.mimeType = mimeType; }

        public String getData() { return data; }
        public void setData(String data) { this.data = data; }

        public DataSource getDataSource() {
            return new DataSource() {
                @Override
                public InputStream getInputStream() throws IOException {
                    return new ByteArrayInputStream(Base64.decode(data));
                }

                @Override
                public OutputStream getOutputStream() throws IOException {
                    return null;
                }

                @Override
                public String getContentType() {
                    return mimeType;
                }

                @Override
                public String getName() {
                    return fileName;
                }
            };
        }
    }
}