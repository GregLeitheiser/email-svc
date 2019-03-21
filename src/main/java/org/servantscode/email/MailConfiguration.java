package org.servantscode.email;

import java.util.Properties;

public class MailConfiguration {
    private boolean requireAuth;
    private boolean useSsl;
    private boolean useTls;
    private String smtpHost;
    private int smtpPort;

    public MailConfiguration() {}

    public Properties getConfigProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", requireAuth);
        props.put("mail.smtp.ssl.enable", Boolean.toString(useSsl));
        props.put("mail.smtp.starttls.enable", Boolean.toString(useTls));
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", Integer.toString(smtpPort));
        return props;
    }

    // ----- Accessors -----
    public boolean isRequireAuth() { return requireAuth; }
    public void setRequireAuth(boolean requireAuth) { this.requireAuth = requireAuth; }

    public boolean isUseTls() { return useTls; }
    public void setUseTls(boolean useTls) { this.useTls = useTls; }

    public boolean isUseSsl() { return useSsl; }
    public void setUseSsl(boolean useSsl) { this.useSsl = useSsl; }

    public String getSmtpHost() { return smtpHost; }
    public void setSmtpHost(String smtpHost) { this.smtpHost = smtpHost; }

    public int getSmtpPort() { return smtpPort; }
    public void setSmtpPort(int smtpPort) { this.smtpPort = smtpPort; }
}
