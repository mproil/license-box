package com.licensebox.ui.page.administration;


import com.licensebox.bl.requests.AppAdminLocal;
import com.licensebox.ui.Helper;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.security.auth.login.LoginException;

/**
 *
 * @author mpaltsev
 */
@ManagedBean
@ViewScoped
public class ManageAdminManagedBean implements Serializable {
    
    @EJB
    private AppAdminLocal appAdmin;

    private String ldapConnectionString;
    private String ldapSearchBase;
    private String ldapUsername;
    private String ldapPassword;
    private boolean passwordChanged;

    public String getLdapConnectionString() {
        return ldapConnectionString;
    }

    public void setLdapConnectionString(String ldapConnectionString) {
        this.ldapConnectionString = ldapConnectionString;
    }

    public String getLdapSearchBase() {
        return ldapSearchBase;
    }

    public void setLdapSearchBase(String ldapSearchBase) {
        this.ldapSearchBase = ldapSearchBase;
    }

    public String getLdapUsername() {
        return ldapUsername;
    }

    public void setLdapUsername(String ldapUsername) {
        this.ldapUsername = ldapUsername;
    }

    public String getLdapPassword() {
        return ldapPassword;
    }

    public void setLdapPassword(String ldapPassword) {
        this.ldapPassword = ldapPassword;
    }
    
    
    
    /**
     * Creates a new instance of ManageAdminManagedBean
     */
    public ManageAdminManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        this.ldapConnectionString = this.appAdmin.getLdapServer();
        this.ldapSearchBase = this.appAdmin.getLdapSearchBase();
        this.ldapUsername = this.appAdmin.getLdapUsername();
        this.ldapPassword = this.appAdmin.getLdapPassword();
        this.passwordChanged = false;
    }
    
    public void changePassword(AjaxBehaviorEvent event) {
        this.passwordChanged = true;
    }
    
    public void updateConnection() {
        try {
            this.appAdmin.updateLdapSettings(ldapConnectionString, ldapSearchBase, ldapUsername, ldapPassword, passwordChanged);
            Helper.addFacesMessage(Helper.INFO, "Data updated successfully");
        } catch (LoginException ex) {
            Helper.addFacesMessage(Helper.ERROR, ex.getMessage());
        }
    }
    
}
