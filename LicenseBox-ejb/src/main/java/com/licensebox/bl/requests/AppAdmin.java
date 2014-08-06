package com.licensebox.bl.requests;

import com.licensebox.bl.Helper;
import java.util.Hashtable;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.security.auth.login.LoginException;

/**
 *
 * @author mpaltsev
 */
@Stateless
public class AppAdmin implements AppAdminLocal {
    
    @PostConstruct
    public void init() {

    }

    @Override
    public String getLdapServer() {
        return getProperty(Helper.LDAP_SERVER);
    }

    @Override
    public void setLdapServer(String ldapServer) {
        Helper.loadPropertiesFile().setProperty(Helper.LDAP_SERVER, ldapServer);
    }

    @Override
    public String getLdapSearchBase() {
        return getProperty(Helper.LDAP_SEARCH_BASE);
    }
    
    @Override
    public void setLdapSearchBase(String searchBase) {
        Helper.loadPropertiesFile().setProperty(Helper.LDAP_SEARCH_BASE, searchBase);
    }
    
    @Override
    public String getLdapUsername() {
        return getProperty(Helper.LDAP_USERNAME);
    }

    @Override
    public void setLdapUsername(String ldapUsername) {
        Helper.loadPropertiesFile().setProperty(Helper.LDAP_USERNAME, ldapUsername);
    }

    @Override
    public String getLdapPassword() {
        return "*********";
    }

    @Override
    public void setLdapPassword(String ldapPassword) {
        Helper.loadPropertiesFile().setProperty(Helper.LDAP_PASSWORD, ldapPassword);
    }
    
    
    
    private String getProperty(String propName) {
        Properties props = Helper.loadPropertiesFile();
        String retVal = props.getProperty(propName);
        return (retVal != null) ? retVal : "";
    }

    @Override
    public void updateLdapSettings(String server, String searchBase, String username, String password, boolean passwordChanged) throws LoginException {
        tryToConnect(server, username, (passwordChanged) ? password : getProperty(Helper.LDAP_PASSWORD));
        
        setLdapServer(server);
        setLdapUsername(username);
        setLdapSearchBase(searchBase);
        if (passwordChanged) {
            setLdapPassword(password);
        }
    }

    private void tryToConnect(String server, String username, String string) throws LoginException {
        LdapContext ctx = null;
        try {
            Hashtable<String, String> env = generateHashtable(server, username, string);
            ctx = new InitialLdapContext(env, null);
        } catch (NamingException ex) {
            throw new LoginException(ex.getMessage());
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (NamingException ex) {
                }
            }
        }
    }
    
    private static Hashtable<String, String> generateHashtable(String server, String username, String password) {
        @SuppressWarnings("UseOfObsoleteCollectionType")
        Hashtable<String, String> retVal = new Hashtable<>();
        
        String securityPrincipal = username;
        
        retVal.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        retVal.put(Context.PROVIDER_URL, server);
        retVal.put(Context.SECURITY_PRINCIPAL, securityPrincipal);
        retVal.put(Context.SECURITY_CREDENTIALS, password);
        
        return retVal;
    }

}
