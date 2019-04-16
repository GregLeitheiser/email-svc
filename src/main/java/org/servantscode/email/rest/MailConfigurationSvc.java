package org.servantscode.email.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.servantscode.commons.db.ConfigDB;
import org.servantscode.commons.rest.SCServiceBase;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Map;

import static org.servantscode.commons.ConfigUtils.encryptConfig;
import static org.servantscode.commons.StringUtils.isSet;

@Path("/email/config")
public class MailConfigurationSvc extends SCServiceBase {
    private static final Logger LOG = LogManager.getLogger(MailConfigurationSvc.class);

    private ConfigDB db;

    public MailConfigurationSvc() {
        this.db = new ConfigDB();
    }

    @GET @Produces(MediaType.APPLICATION_JSON)
    public MailConfiguration getConfig() {
        verifyUserAccess("admin.mail.config.read");

        try {
            return new MailConfiguration(db.getConfigurations("mail"));
        } catch (Throwable t) {
            LOG.error("Failed to get mail configuration", t);
            throw new WebApplicationException("Failed to get mail configuration", t);
        }
    }

    @PUT @Consumes(MediaType.APPLICATION_JSON)
    public void updateConfig(MailConfiguration config) {
        verifyUserAccess("admin.mail.config.update");

        try {
            Map<String, String> configMap = config.toMap();
            String password = configMap.get("mail.user.password");
            if(isSet(password))
                configMap.put("mail.user.password", encryptConfig(password));
            db.patchConfigurations(configMap);
        } catch (Throwable t) {
            LOG.error("Failed to patch mail configuration", t);
            throw new WebApplicationException("Failed to patch mail configuration", t);
        }
    }

    @DELETE
    public void deleteConfig() {
        verifyUserAccess("admin.mail.config.delete");

        try {
            db.deleteConfigurations(MailConfiguration.configFields());
        } catch (Throwable t) {
            LOG.error("Failed to delete mail configuration", t);
            throw new WebApplicationException("Failed to delete mail configuration", t);
        }
    }
}
