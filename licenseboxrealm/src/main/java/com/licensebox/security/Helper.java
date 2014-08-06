/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.licensebox.security;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author mpaltsev
 */
public final class Helper {
    
    public static final String LDAP_INITIAL_CONTEXT_FACTORY = "LDAP_INITIAL_CONTEXT_FACTORY";
    public static final String LDAP_SECURITY_AUTHENTICATION = "LDAP_SECURITY_AUTHENTICATION";
    public static final String LDAP_PROVIDER_URL = "LDAP_PROVIDER_URL";
    public static final String LDAP_SECURITY_CREDENTIAL = "LDAP_SECURITY_CREDENTIAL";
    public static final String LDAP_SECURITY_PRINCIPAL = "LDAP_SECURITY_PRINCIPAL";
    
    public static final String JDBC_PASS = "JDBC_PASS";
    public static final String JDBC_USER = "JDBC_USER";
    public static final String JDBC_DB_URL = "JDBC_DB_URL";
    public static final String JDBC_DRIVER = "JDBC_DRIVER";
    
    public static final String SHA_ALGORITHM = "SHA-256";
    
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
    
}
