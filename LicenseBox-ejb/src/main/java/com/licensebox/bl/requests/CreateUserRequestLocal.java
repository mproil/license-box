package com.licensebox.bl.requests;

import javax.ejb.Local;

/**
 * This interface defines all the methods that can be used by the system
 * administrator
 * 
 * @author Michael Paltsev & Anna Guzman
 */
@Local
public interface CreateUserRequestLocal {

    /**
     * This method creates a new password for a username and sends it to him via
     * email
     * @param username The username of the user to whom you want to create a new password
     * @param fullName The full name of the user
     * @param email  The email of the user
     */
    void setNewPasswordForExistingUser(String username, String fullName, String email);

    /**
     * Creates a new user and adds him to the database
     * @param username The username of the new user
     * @param firstName The first name of the new user
     * @param lastName The last name of the new user
     * @param email The email of the new user
     * @param teamId The team id of the new user (can be null)
     */
    void createNewUser(String username, String firstName, String lastName, String email, Integer teamId);
    
    /**
     * This method adds the manager role to a specific user
     * @param username The username to whom you want to give the manager role
     */
    void setNewManager(String username);
    
    /**
     * This method removes the manager role from a specific user
     * @param username The username from whom you want to remove the manager role
     */
    void removeManager(String username);
 
    /**
     * This method adds the license manager role to a specific user
     * @param username The username to whom you want to give the license manager role
     */
    void setLicenseManager(String username);
    
    /**
     * This method removes the license manager role from a specific user
     * @param username The username from whom you want to remove the license manager role
     */
    void removeLicenseManager(String username);
    
    /**
     * This method disables a specific user. A user can be disabled only if he
     * has one role which is the user role. Disabling a user will ban the user
     * from accessing the license box application but it won't remove his licenses.
     * @param username The username that you want to disable
     */
    void disableUser(String username);
    
    /**
     * This method enables a specific user. After a user is enabled he will have
     * only the user role.
     * @param username The user that you want to enable
     */
    void enableUser(String username);
    
}