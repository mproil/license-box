package com.licensebox.bl.requests;

import com.licensebox.bl.Helper;
import com.licensebox.bl.email.LicenseBoxEmailLocal;
import com.licensebox.bl.exception.LicenseBoxUncheckedException;
import com.licensebox.db.dao.AppRoleDaoLocal;
import com.licensebox.db.dao.AppUserDaoLocal;
import com.licensebox.db.dao.TeamDaoLocal;
import com.licensebox.db.entity.AppRole;
import com.licensebox.db.entity.AppUser;
import com.licensebox.db.entity.Team;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

/**
 * This EJB handles all the Business Logic regarding the users
 * 
 * @author Michael Paltsev & Anna Guzman
 */
@Stateless
public class CreateUserRequest implements CreateUserRequestLocal {

    //<editor-fold defaultstate="collapsed" desc="FINAL Properties">
    private final int PASS_LENGTH = 8;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="EJB Injection">
    @EJB
    private AppUserDaoLocal appUserDao;
    
    @EJB
    private LicenseBoxEmailLocal licenseBoxEmail;
    
    @EJB
    private AppRoleDaoLocal appRoleDao;
    
    @EJB
    private TeamDaoLocal teamDao;
    //</editor-fold>
    
    @PostConstruct
    public void init() {
        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setNewPasswordForExistingUser(String username, String fullName, String email) {
        String password = createRandomPassword(PASS_LENGTH);
        String hashedPassword = Helper.createHash(password, Helper.SHA_ALGORITHM);
        this.appUserDao.setUserPassword(username, hashedPassword);
        String subject = "Message From LicenseBox";
        String message = String.format("Dear %s,\n\nA new password was generated for you automatically by our staff.\n\n"
                + "The password is: %s\n\nPlease keep it in a safe place.\n\n\nSincerely,\nThe LicenseBox Team", 
                new Object[] {fullName, password});
        this.licenseBoxEmail.sendMessage(fullName, subject, message, email);
    }
    
    
    /**
     * This method creates a random password
     *
     * @param length The length of the new password
     * @return A new random password
     */
    private String createRandomPassword(int length) {
        return Long.toHexString(Double.doubleToLongBits(Math.random())).substring(0, length);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createNewUser(boolean ldapUser, String username, String firstName, String lastName, String email, Integer teamId) {
        String password = this.createRandomPassword(PASS_LENGTH);
        AppUser appUser = new AppUser();
        appUser.setUsername(username);
        if (!ldapUser) {
            String hashedPassword = Helper.createHash(password, Helper.SHA_ALGORITHM);
            appUser.setPassword(hashedPassword);
        }
        
        appUser.setLdapUser(ldapUser);
        appUser.setFirstName(firstName);
        appUser.setLastName(lastName);
        
        // Set the default role for the user.
        AppRole appRole = this.appRoleDao.findRole(AppRole.RoleName.USER);
        List<AppRole> appRoleList = new LinkedList<>();
        
        appRoleList.add(appRole);
        appUser.setAppRoleList(appRoleList);
        appUser.setEmail(email);
        
        Team team = (teamId != null) ? this.teamDao.getTeamById(teamId) : null;
        
        if ((teamId != null) && (team != null)) {
            appUser.setTeam(team);                                                  
        }
        this.appUserDao.createNewUser(appUser);
        
        // Prepare the email for the user
        String fullName = String.format("%s %s", new Object[] {firstName, lastName});
        if (!ldapUser) {
            createAndSendEmailToNewUser(fullName, email, username, password);
        } else {
            createAndSendEmailToNewUser(fullName, email);
        }
    }
    
    /**
     * This method creates and sends a generic email to a new user
     * @param fullName
     * @param email
     * @param username
     * @param password 
     */
    private void createAndSendEmailToNewUser(String fullName, String email,
            String username, String password) {
        String subject = "Welcome to LicenseBox";
        String message = String.format("Dear %s,\n\nA new user was created for you in LicenseBox.\n\n"
                + "Your username is: %s \n\n"
                + "Your password is: %s \n\n"
                + "Please keep it in a safe place.\n\n\n"
                + "Sincerely,\nThe LicenseBox Team", 
                new Object[] {fullName, username, password});
        this.licenseBoxEmail.sendMessage(fullName, subject, message, email);
    }
    
    private void createAndSendEmailToNewUser(String fullName, String email) {
        String subject = "Welcome to LicenseBox";
        String message = String.format("Dear %s,\n\nA new user was created for you in LicenseBox.\n\n"
                + "Please use your enterprise user to login to the system\n\n"
                + "Sincerely,\nThe LicenseBox Team", 
                new Object[] {fullName});
        this.licenseBoxEmail.sendMessage(fullName, subject, message, email);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNewManager(String username) {
        if (needToAddManagerRole(username)) {
            AppUser user = this.appUserDao.getUserByUsername(username);
            AppRole appRole = this.appRoleDao.findRole(AppRole.RoleName.MANAGER);

            List<AppRole> appRoleList = user.getAppRoleList();
            appRoleList.add(appRole);
            this.appUserDao.updateUser(user);
        }
    }
    
    private boolean needToAddManagerRole(String username) {
        List<AppRole.RoleName> teamManagerRoles 
                = this.appUserDao.getUserRoles(username);
        return !teamManagerRoles.contains(AppRole.RoleName.MANAGER);
    }
    
    private boolean needToRemoveManagerRole(String username) {
        return !needToAddManagerRole(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeManager(String username) {
        if (needToRemoveManagerRole(username)) {
            AppUser user = this.appUserDao.getUserByUsername(username);
            AppRole appRole = this.appRoleDao.findRole(AppRole.RoleName.MANAGER);

            List<AppRole> appRoleList = user.getAppRoleList();
            appRoleList.remove(appRole);
            this.appUserDao.updateUser(user);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLicenseManager(String username) {
        if (needToAddLicenseManagerRole(username)) {
            AppUser user = this.appUserDao.getUserByUsername(username);
            AppRole appRole = this.appRoleDao.findRole(AppRole.RoleName.LICENSE_MANAGER);

            List<AppRole> appRoleList = user.getAppRoleList();
            appRoleList.add(appRole);
            this.appUserDao.updateUser(user);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeLicenseManager(String username) {
        if (needToRemoveLicenseManagerRole(username)) {
            AppUser user = this.appUserDao.getUserByUsername(username);
            AppRole appRole = this.appRoleDao.findRole(AppRole.RoleName.LICENSE_MANAGER);

            List<AppRole> appRoleList = user.getAppRoleList();
            appRoleList.remove(appRole);
            this.appUserDao.updateUser(user);
        }
    }
    
    private boolean needToAddLicenseManagerRole(String username) {
        List<AppRole.RoleName> teamManagerRoles 
                = this.appUserDao.getUserRoles(username);
        return !teamManagerRoles.contains(AppRole.RoleName.LICENSE_MANAGER);
    }
    
    private boolean needToRemoveLicenseManagerRole(String username) {
        return !needToAddLicenseManagerRole(username);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void disableUser(String username) {
        if (username != null) {
            AppUser appUser = this.appUserDao.getUserByUsername(username);
            if (appUser == null) {
                throw new LicenseBoxUncheckedException("A user with the username: " + username + " does not exist");
            }
            
            List<AppRole> appRoles = appUser.getAppRoleList();
            if (appRoles.isEmpty()) {
                throw new LicenseBoxUncheckedException("The user: " + username + " is disabled already");
            }
            if (appRoles.size() > 1) {
                throw new LicenseBoxUncheckedException("To disable a user, please make sure that he only has the role " + AppRole.RoleName.USER.getValue());
            }
            
            appRoles.remove(0); // there should only be one role in this list
            
            this.appUserDao.updateUser(appUser);
            
            String userFullName = appUser.getFirstName() + " " + appUser.getLastName();
            String subject = "Your User Has Been Disabled";
            String email = appUser.getEmail();
            String message = "Your user in the LicenseBox application has been disabled.\n\n"
                    + "Please contact the administrator.\n\n"
                    + "Sincerely,\n\nThe LicenseBox Team";
            
            this.licenseBoxEmail.sendMessage(userFullName, subject, message, email);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void enableUser(String username) {
        if (username != null) {
            AppUser appUser = this.appUserDao.getUserByUsername(username);
            if (appUser == null) {
                throw new LicenseBoxUncheckedException("A user with the username: " + username + " does not exist");
            }
            
            List<AppRole> appRoles = appUser.getAppRoleList();
            AppRole userAppRole = this.appRoleDao.findRole(AppRole.RoleName.USER);
            if ((!appRoles.isEmpty()) && (appRoles.contains(userAppRole))) {
                throw new LicenseBoxUncheckedException("The user: " + username + " is already enabled");
            }
            
            appRoles.add(userAppRole); // there should only be one role in this list
            
            this.appUserDao.updateUser(appUser);
            
            String userFullName = appUser.getFirstName() + " " + appUser.getLastName();
            String subject = "Your User Has Been Enabled";
            String email = appUser.getEmail();
            String message = "Your user in the LicenseBox application has been enabled.\n\n"
                    + "Please use your previous password.\n\n"
                    + "Sincerely,\n\nThe LicenseBox Team";
            
            this.licenseBoxEmail.sendMessage(userFullName, subject, message, email);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SearchResult searchForLdapUser(String username) {
        Properties props = Helper.loadPropertiesFile();
        
        if (props == null) {
            return null;
        }
        
        Hashtable<String, String> env = generateHashtable(props);
        
        return getResultsFromLdap(env, props, username);
    }
        
    private Hashtable<String, String> generateHashtable(Properties props) {
        Hashtable<String, String> retVal = new Hashtable<>();
        
        //String ldapServer = props.getProperty("LDAP_SERVER");
        String ldapServer = props.getProperty(Helper.LDAP_SERVER);
        //String ldapUsername = props.getProperty("LDAP_USERNAME");
        String ldapUsername = props.getProperty(Helper.LDAP_USERNAME);
        //String ldapPassword = props.getProperty("LDAP_PASSWORD");
        String ldapPassword = props.getProperty(Helper.LDAP_PASSWORD);
        
        retVal.put(Context.SECURITY_AUTHENTICATION, "simple");
        if (ldapUsername != null) {
            retVal.put(Context.SECURITY_PRINCIPAL, ldapUsername);
        } else {
            return null;
        }
        
        if (ldapPassword != null) {
            retVal.put(Context.SECURITY_CREDENTIALS, ldapPassword);
        } else {
            return null;
        }
        
        if (ldapServer != null) {
            retVal.put(Context.PROVIDER_URL, ldapServer);
        } else {
            return null;
        }
        
        retVal.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        
        return retVal;
    }
    
    private SearchResult findAccountByAccountName(DirContext ctx, String ldapSearchBase, String accountName) throws NamingException {

        String searchFilter = "(&(objectClass=user)(sAMAccountName=" + accountName + "))";

        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        NamingEnumeration<SearchResult> results = ctx.search(ldapSearchBase, searchFilter, searchControls);

        SearchResult searchResult = null;
        if(results.hasMoreElements()) {
             searchResult = (SearchResult) results.nextElement();

            //make sure there is not another item available, there should be only 1 match
//            if(results.hasMoreElements()) {
//                System.err.println("Matched multiple users for the accountName: " + accountName);
//                return null;
//            }
        }
        
        return searchResult;
    }
    
    private SearchResult getResultsFromLdap(Hashtable<String, String> env, Properties props, String accountName) {
        SearchResult retVal;
        LdapContext ctx = null;
        try {
            ctx = new InitialLdapContext(env, null);
            //String ldapSearchBase = props.getProperty("LDAP_SEARCH_BASE");
            String ldapSearchBase = props.getProperty(Helper.LDAP_SEARCH_BASE);
            retVal = findAccountByAccountName(ctx, ldapSearchBase, accountName);
        } catch (NamingException ex) {
            return null;
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (NamingException ex) {
                }
            }
        }
        return retVal;
    }
}