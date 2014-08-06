/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.licensebox.bl.requests;

import javax.ejb.Local;
import javax.security.auth.login.LoginException;

/**
 *
 * @author mpaltsev
 */
@Local
public interface AppAdminLocal {

    String getLdapServer();

    void setLdapServer(String ldapServer);

    String getLdapSearchBase();

    void setLdapSearchBase(String searchBase);

    String getLdapUsername();

    void setLdapUsername(String ldapUsername);

    String getLdapPassword();

    void setLdapPassword(String ldapPassword);

    void updateLdapSettings(String server, String searchBase, String username, String password, boolean passwordChanged) throws LoginException ;
    
}
