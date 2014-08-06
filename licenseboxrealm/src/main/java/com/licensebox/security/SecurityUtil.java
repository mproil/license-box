package com.licensebox.security;

import com.sun.enterprise.security.auth.realm.Realm;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
 * This class is responsible for the authentication of the user.
 * 
 * @author Michael Paltsev
 */
public class SecurityUtil {

    
    
    private SecurityUtil() {
        
    }
        
    /**
     * Try to authenticate the user. Throw a LoginException if the user cannot 
     * be authenticated
     * @param username The username that you want to authenticate
     * @param password His password
     * @param realm The Realm
     * @throws LoginException In case the authentication fails
     */
    public static void authenticateUser(String username, String password, Realm realm) throws LoginException {
        if ((username == null) || (password == null)) {
            throw new LoginException("Username or Password are null");
        }
        
        if (isLdapUser(username, realm)) {
            // Check authentication using LDAP
            Logger.getLogger(SecurityUtil.class.getName()).log(Level.INFO, String.format("%s is an LDAP user", username));
            validateLdap(username, password, realm);
        } else {
            // Check authentication using JDBC
            Logger.getLogger(SecurityUtil.class.getName()).log(Level.INFO, String.format("%s is a SQL user", username));
            validateJdbc(username, password, realm);
        }
    }
    
    /**
     * Returns a List of Strings representing the groups this user belongs to
     * @param username The username whose groups you want to get
     * @param realm The Reals
     * @return A List of Strings representing the groups
     * @throws LoginException If the data that was supplied to the Realm in the configuration section
     * of the server is not valid
     */
    public static List<String> getGroups(String username, Realm realm) throws LoginException {
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
    
    /**
     * Tries to match the username and the password in the database
     * @param username The username to match
     * @param password The password to match
     * @param realm The Realm from which to get the additional information (about the DB, etc)
     * @throws LoginException If the authentication failed or if we could not connect to the DB
     */
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
    
    
    /**
     * Checks if the user is an LDAP user
     * @param username The username you want to check
     * @param realm The Realm from which to get the additional information
     * @return True if the user is an LDAP user and false otherwise
     */
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
    
    /**
     * Generates a JDBC connection
     * @param realm The Realm that holds the data to create the connection
     * @return A JDBC connection
     * @throws SQLException If we could not connect to the DB
     * @throws ClassNotFoundException If we were unable to load the driver
     */
    private static Connection generateJdbcConnection(Realm realm) throws SQLException, ClassNotFoundException {
        String driver = realm.getProperty(Helper.JDBC_DRIVER);
        String connStr = realm.getProperty(Helper.JDBC_DB_URL);

        Properties props = generateConnectionProperties(realm);
        
        return getConnection(driver, connStr, props);
    }
    
    /**
     * Generates connection properties
     * @param realm The Realm from which to get the properties
     * @return A Properties object that contains the connection credentials
     */
    private static Properties generateConnectionProperties(Realm realm) {
        Properties retVal = new Properties();
        retVal.put(Helper.JDBC_USER, realm.getProperty(Helper.JDBC_USER));
        retVal.put(Helper.JDBC_PASS, realm.getProperty(Helper.JDBC_PASS));
        return retVal;
    }
        
    /**
     * Generates a connection from a driver, a connection string and the credentials
     * @param driver The driver name
     * @param connectionString The connection string
     * @param credentials Properties object that contains the credentials
     * @return A JDBC connection object
     * @throws SQLException If a SQL Exception occurs
     * @throws ClassNotFoundException If the driver cannot be loaded
     */
    private static Connection getConnection(String driver, String connectionString, Properties credentials) throws SQLException, ClassNotFoundException {
        Class.forName(driver);
        //return DriverManager.getConnection(connectionString, credentials);
        return DriverManager.getConnection(connectionString, credentials.getProperty(Helper.JDBC_USER), credentials.getProperty(Helper.JDBC_PASS));
    }
    
    /**
     * Generates an LDAP query for the DB
     * @param conn The connection object
     * @param username The username you want to integrate into the query
     * @return A PreparedStatement object
     * @throws SQLException An exception if the statement cannot be created
     */
    private static PreparedStatement generateLdapQuery(Connection conn, String username) throws SQLException {
        String query = "SELECT ldap FROM LicenseBoxDB.app_user WHERE username= ? ; ";
        PreparedStatement retVal = conn.prepareStatement(query);
        retVal.setString(1, username);
        return retVal;
    }
    
    /**
     * Generates a Password query for the DB
     * @param conn The connection object
     * @param username The username you want to integrate into the query
     * @return A PreparedStatement object
     * @throws SQLException An exception if the statement cannot be created
     */
    private static PreparedStatement generatePasswordQuery(Connection conn, String username) throws SQLException {
        String query = "SELECT password FROM LicenseBoxDB.app_user WHERE username= ? ; ";
        PreparedStatement retVal = conn.prepareStatement(query);
        retVal.setString(1, username);
        return retVal;
    }
    
    /**
     * Validates LDAP connection
     * @param username The username you want to validate
     * @param password His password
     * @param realm The Realm that contains extra information
     * @throws LoginException If the authentication fails
     */
    private static void validateLdap(String username, String password, Realm realm) throws LoginException {
        @SuppressWarnings("UseOfObsoleteCollectionType")
        Hashtable<String, String> env = generateHashtable(username, password, realm);
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
    
    /**
     * Generates a Hashtable for the LDAP connection
     * @param username The username
     * @param password The password
     * @param realm The Realm that holds additional information
     * @return A Hashtable that contains all the information
     */
    private static Hashtable<String, String> generateHashtable(String username, String password, Realm realm) {
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
    
    /**
     * Generates a Group query for the DB
     * @param conn The connection object
     * @param username The username you want to integrate into the query
     * @return A PreparedStatement object
     * @throws SQLException An exception if the statement cannot be created
     */   
    private static PreparedStatement generateGroupQuery(Connection conn, String username) throws SQLException {
        String query = "SELECT role_name FROM LicenseBoxDB.user_role WHERE username= ? ; ";
        PreparedStatement retVal = conn.prepareStatement(query);
        retVal.setString(1, username);
        return retVal;
    }
    
}