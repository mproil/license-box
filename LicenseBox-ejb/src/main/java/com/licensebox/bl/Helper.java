package com.licensebox.bl;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

/**
 *
 * @author mpaltsev
 */
public class Helper {
    
    
//    public static final String LDAP_INITIAL_CONTEXT_FACTORY = "LDAP_INITIAL_CONTEXT_FACTORY";
//    public static final String LDAP_PROVIDER_URL = "LDAP_PROVIDER_URL";
//    public static final String LDAP_SECURITY_AUTHENTICATION = "LDAP_SECURITY_AUTHENTICATION";
//    public static final String LDAP_SECURITY_PRINCIPAL = "LDAP_SECURITY_PRINCIPAL";
//    public static final String LDAP_SECURITY_CREDENTIALS = "LDAP_SECURITY_CREDENTIALS";
    public static final String LDAP_SERVER = "LDAP_SERVER";
    public static final String LDAP_SEARCH_BASE = "LDAP_SEARCH_BASE";
    public static final String LDAP_USERNAME = "LDAP_USERNAME";
    public static final String LDAP_PASSWORD = "LDAP_PASSWORD";
    
    public static final String SHA_ALGORITHM = "SHA-256";
    
    private static final String PROPERTIES_FILENAME = "licensebox_config.properties";
    
    private Helper() {
        
    }
    
    

    /**
     * This method creates a hash using the algorithm defined in this
     * class
     *
     * @param password The password we want to hash
     * @param algorithm the value of algorithm
     * @return A hashed password
     */
    public static String createHash(String password, String algorithm) {
        MessageDigest messageDigest;
        String retVal;
        try {
            messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(password.getBytes());
            byte[] shaDigest = messageDigest.digest();
            retVal = HexBin.encode(shaDigest).toLowerCase();
        }
        catch (NoSuchAlgorithmException ex) {
            retVal = null;
        }
        return retVal;
    }

    /**
     * Loads the properties for this application
     * @return Properties that are loaded. If no properties file is found, an
     * empty properties object is returned
     */
    public static Properties loadPropertiesFile() {
        Properties props = new Properties();
        try {
            props.load(Helper.class.getResourceAsStream(PROPERTIES_FILENAME));
        }
        catch (IOException ex) {
            props = null;
        }
        return props;
    }
    
}
