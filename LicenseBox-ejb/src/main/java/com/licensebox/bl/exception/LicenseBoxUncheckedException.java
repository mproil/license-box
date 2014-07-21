package com.licensebox.bl.exception;

import javax.ejb.EJBException;

/**
 * This class extends the EJBException class and used inside EJB's.
 * Throwing this exception inside an EJB method will result in a roll back.
 * 
 * @author Michael Paltsev & Anna Guzman
 */
public class LicenseBoxUncheckedException extends EJBException {
    
    /**
     * Create a new exception
     * @param message The message in the exception
     */
    public LicenseBoxUncheckedException(String message) {
        super(message);
    }
    
}
