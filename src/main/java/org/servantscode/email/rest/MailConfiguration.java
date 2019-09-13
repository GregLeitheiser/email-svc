package org.servantscode.email.rest;

import java.util.*;

import static org.servantscode.commons.StringUtils.isSet;

public class MailConfiguration {
    public static final String OBFUSCATED_FIELD = "********";
    private boolean requireAuth;
    private boolean useSsl;
    private boolean useTls;
    private String smtpHost;
    private int smtpPort;

    private String sendFromUser;
    private String emailAccount;
    private String accountPassword;

    public MailConfiguration() {}

    protected MailConfiguration(Map<String, String> config) {
        this.requireAuth = Boolean.parseBoolean(config.get("mail.smtp.auth"));
        this.useSsl = Boolean.parseBoolean(config.get("mail.smtp.ssl.enable"));
        this.useTls = Boolean.parseBoolean(config.get("mail.smtp.starttls.enable"));
        this.smtpHost = config.get("mail.smtp.host");
        if(isSet(config.get("mail.smtp.port")))
            this.smtpPort = Integer.parseInt(config.get("mail.smtp.port"));

        this.sendFromUser = config.get("mail.smtp.sendFromUser");
        this.emailAccount = config.get("mail.user.account");
        if(isSet(config.get("mail.user.password")))
            this.accountPassword = OBFUSCATED_FIELD;
    }

    protected Map<String, String> toMap() {
        Map<String, String> props = new HashMap<>();
        props.put("mail.smtp.auth", Boolean.toString(requireAuth));
        props.put("mail.smtp.ssl.enable", Boolean.toString(useSsl));
        props.put("mail.smtp.starttls.enable", Boolean.toString(useTls));
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", Integer.toString(smtpPort));
        props.put("mail.smtp.sendFromUser", sendFromUser);
        props.put("mail.user.account", emailAccount);
        if(isSet(accountPassword) && !accountPassword.equals(OBFUSCATED_FIELD))
            props.put("mail.user.password", accountPassword);
        return props;
    }

    protected static Set<String> configFields() {
        Set<String> fields = new HashSet<>();
        fields.add("mail.smtp.auth");
        fields.add("mail.smtp.ssl.enable");
        fields.add("mail.smtp.starttls.enable");
        fields.add("mail.smtp.host");
        fields.add("mail.smtp.port");
        fields.add("mail.smtp.sendFromUser");
        fields.add("mail.user.account");
        fields.add("mail.user.password");
        return fields;
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

    public String getSendFromUser() { return sendFromUser; }
    public void setSendFromUser(String sendFromUser) { this.sendFromUser = sendFromUser; }

    public String getEmailAccount() { return emailAccount; }
    public void setEmailAccount(String emailAccount) { this.emailAccount = emailAccount; }

    public String getAccountPassword() { return accountPassword; }
    public void setAccountPassword(String accountPassword) { this.accountPassword = accountPassword; }
}
