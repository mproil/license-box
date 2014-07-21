package com.licensebox.bl.email;

import javax.ejb.Local;

/**
 * This Interface defines the methods that will be used to send email
 * 
 * @author Michael Paltsev & Anna Guzman
 */
@Local
public interface LicenseBoxEmailLocal {

    /**
     * This method sends an email to a recipient 
     * @param toFullName The full name of the recipient
     * @param subject The subject of the email
     * @param message The message that you want to send
     * @param toEmail The email to which you want to send the email
     */
    void sendMessage(String toFullName, String subject, String message, String toEmail);
    
}
