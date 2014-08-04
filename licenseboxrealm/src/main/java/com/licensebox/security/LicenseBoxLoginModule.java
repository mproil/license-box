package com.licensebox.security;

import com.sun.appserv.security.AppservPasswordLoginModule;
import java.util.List;
import javax.security.auth.login.LoginException;

/**
 *
 * @author Michael Paltsev
 */
public class LicenseBoxLoginModule extends AppservPasswordLoginModule {
    
    @Override
    protected void authenticateUser() throws LoginException {
        
        SecurityUtil.authenticateUser(_username, _password, _currentRealm);
        
        List<String> groupList = SecurityUtil.getGroups(_username, _currentRealm);
        String[] groups;
        if ((groupList != null) && (!groupList.isEmpty())) {
            groups = groupList.toArray(new String[groupList.size()]);
        } else {
            groups = new String[0];
        }
        
        commitUserAuthentication(groups);
    }
    
}
