package com.licensebox.security;

import com.sun.enterprise.security.auth.realm.Realm;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.security.auth.login.LoginException;

/**
 *
 * @author mpaltsev
 */
public class SecurityUtil {

    
    
    private SecurityUtil() {
        
    }
        
    public static void authenticateUser(String username, String password, Realm realm) throws LoginException {
        if ((username == null) || (password == null)) {
            throw new LoginException("Username or Password are null");
        }
        
        if (isLdapUser(username, realm)) {
            // Check authentication using LDAP
            Logger.getLogger(SecurityUtil.class.getName()).log(Level.INFO, "This is an LDAP user");
            validateLdap(username, password, realm);
        } else {
            // Check authentication using JDBC
            Logger.getLogger(SecurityUtil.class.getName()).log(Level.INFO, "This is a JDBC user");
            validateJdbc(username, password, realm);
        }
    }
    
    
    public static List<String> getGroups(String username, Realm realm) throws LoginException {
        return getUserGroups(username, realm);
    }
    
    
    
    private static void validateJdbc(String username, String password, Realm realm) throws LoginException {

        try (Connection conn = generateJdbcConnection(realm)) {
            String hashedPassword = Helper.createHash(password, Helper.SHA_ALGORITHM);
            ResultSet resultSet = generatePasswordQuery(conn, username).executeQuery();
            if (!(resultSet.first()) || !(resultSet.getString(1).equals(hashedPassword))) {
                throw new LoginException("Password does not match");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            throw new LoginException(ex.getMessage());
        }
    }
    
    
    private static boolean isLdapUser(String username, Realm realm) {
        
        boolean retVal = false;
        
        try (Connection conn = generateJdbcConnection(realm)) {
            ResultSet resultSet = generateLdapQuery(conn, username).executeQuery();
            if (resultSet.first()) {
                retVal = resultSet.getBoolean(1);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(SecurityUtil.class.getName()).log(Level.WARNING, ex.getMessage());
        }
        
        return retVal;
        
    }
    
    private static Connection generateJdbcConnection(Realm realm) throws SQLException, ClassNotFoundException {
        String driver = realm.getProperty(Helper.JDBC_DRIVER);
        String connStr = realm.getProperty(Helper.JDBC_DB_URL);

        Properties props = generateConnectionProperties(realm);
        
        return getConnection(driver, connStr, props);
    }
    
    private static Properties generateConnectionProperties(Realm realm) {
        Properties retVal = new Properties();
        retVal.put(Helper.JDBC_USER, realm.getProperty(Helper.JDBC_USER));
        retVal.put(Helper.JDBC_PASS, realm.getProperty(Helper.JDBC_PASS));
        return retVal;
    }
        
    private static Connection getConnection(String driver, String connectionString, Properties credentials) throws SQLException, ClassNotFoundException {
        Class.forName(driver);
        //return DriverManager.getConnection(connectionString, credentials);
        return DriverManager.getConnection(connectionString, credentials.getProperty(Helper.JDBC_USER), credentials.getProperty(Helper.JDBC_PASS));
    }
    
    private static PreparedStatement generateLdapQuery(Connection conn, String username) throws SQLException {
        String query = "SELECT ldap FROM LicenseBoxDB.app_user WHERE username= ? ; ";
        PreparedStatement retVal = conn.prepareStatement(query);
        retVal.setString(1, username);
        return retVal;
    }
    
    private static PreparedStatement generatePasswordQuery(Connection conn, String username) throws SQLException {
        String query = "SELECT password FROM LicenseBoxDB.app_user WHERE username= ? ; ";
        PreparedStatement retVal = conn.prepareStatement(query);
        retVal.setString(1, username);
        return retVal;
    }
    
    private static void validateLdap(String username, String password, Realm realm) throws LoginException {
        Hashtable env = generateHashtable(username, password, realm);
        LdapContext ctx = null;
        try {
            ctx = new InitialLdapContext(env, null);
        } catch (NamingException ex) {
            Logger.getLogger(SecurityUtil.class.getName()).log(Level.SEVERE, null, ex);
            throw new LoginException(ex.getMessage());
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (NamingException ex) {
                    Logger.getLogger(SecurityUtil.class.getName()).log(Level.SEVERE, "Cannot close the LDAP context", ex);
                }
            }
        }
    }
    
    private static Hashtable generateHashtable(String username, String password, Realm realm) {
        @SuppressWarnings("UseOfObsoleteCollectionType")
        Hashtable<String, String> retVal = new Hashtable<>();
        
        String securityPrincipal = String.format(realm.getProperty(Helper.LDAP_SECURITY_PRINCIPAL), username);
        
        retVal.put(Context.INITIAL_CONTEXT_FACTORY, realm.getProperty(Helper.LDAP_INITIAL_CONTEXT_FACTORY));
        retVal.put(Context.PROVIDER_URL, realm.getProperty(Helper.LDAP_PROVIDER_URL));
        //retVal.put(Context.SECURITY_AUTHENTICATION, realm.getProperty(Helper.LDAP_SECURITY_AUTHENTICATION));
        retVal.put(Context.SECURITY_PRINCIPAL, securityPrincipal);
        retVal.put(Context.SECURITY_CREDENTIALS, password);
        
        return retVal;
    }
    
    private static List<String> getUserGroups(String username, Realm realm) {
        
        List<String> retVal = null;
        
        try (Connection conn = generateJdbcConnection(realm)) {
            ResultSet resultSet = ((PreparedStatement)generateGroupQuery(conn, username)).executeQuery();
            retVal = new LinkedList<>();
            resultSet.first();
            while (!resultSet.isAfterLast()) {
                retVal.add(resultSet.getString(1));
                resultSet.next();
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(SecurityUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return retVal;
    }
    
    private static PreparedStatement generateGroupQuery(Connection conn, String username) throws SQLException {
        String query = "SELECT role_name FROM LicenseBoxDB.user_role WHERE username= ? ; ";
        PreparedStatement retVal = conn.prepareStatement(query);
        retVal.setString(1, username);
        return retVal;
    }
    
}
