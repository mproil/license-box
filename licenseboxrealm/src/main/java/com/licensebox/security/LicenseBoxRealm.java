package com.licensebox.security;

import com.sun.appserv.security.AppservRealm;
import com.sun.enterprise.security.auth.realm.InvalidOperationException;
import com.sun.enterprise.security.auth.realm.NoSuchUserException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;



/**
 * This is a custom Realm that is used to authenticate users
 * both from an LDAP and a DB.
 * 
 * @author Michael Paltsev
 */
public class LicenseBoxRealm extends AppservRealm {
    
    public static final String PARAM_JAAS_CONTEXT = "jaas-context";
    public static final String AUTH_TYPE = "Authentication done by checking users in JDBC and LDAP";
    
    @Override
    public void init(Properties properties) {
        String propJaasContext = properties.getProperty(PARAM_JAAS_CONTEXT);
        
        if (propJaasContext != null) {
            setProperty(PARAM_JAAS_CONTEXT, propJaasContext);
        }
        
        Logger logger = Logger.getLogger(this.getClass().getName());
        
        String realName = this.getClass().getSimpleName();
        
        logger.log(Level.INFO, "{0} started. ", realName);
        logger.log(Level.INFO, "{0}: {1}", new Object[]{realName, getAuthType()});
        logger.log(Level.INFO, "authentication uses jar file com-licensebox-security");
        
        setProperties(properties);
    }

    @Override
    public String getAuthType() {
        return AUTH_TYPE;
    }

    @Override
    public Enumeration getGroupNames(String username) throws InvalidOperationException, NoSuchUserException {
        try {
            return Collections.enumeration(SecurityUtil.getGroups(username, this));
        } catch (LoginException ex) {
            Logger.getLogger(LicenseBoxRealm.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    private void setProperties(Properties properties) {
        checkAndSetAProperty(Helper.LDAP_INITIAL_CONTEXT_FACTORY, properties);
        checkAndSetAProperty(Helper.LDAP_PROVIDER_URL, properties);
        checkAndSetAProperty(Helper.LDAP_SECURITY_AUTHENTICATION, properties);
        checkAndSetAProperty(Helper.LDAP_SECURITY_PRINCIPAL, properties);
        //checkAndSetAProperty(Helper.LDAP_SECURITY_CREDENTIAL, properties);
        
        checkAndSetAProperty(Helper.JDBC_DRIVER, properties);
        checkAndSetAProperty(Helper.JDBC_DB_URL, properties);
        checkAndSetAProperty(Helper.JDBC_USER, properties);
        checkAndSetAProperty(Helper.JDBC_PASS, properties);
    }

    private void checkAndSetAProperty(String propertyToSet, Properties properties) {
        String propertyValue = properties.getProperty(propertyToSet);
        if (propertyValue == null) {
            propertyValue = "";
        }
        this.setProperty(propertyToSet, propertyValue);
    }
    
}
