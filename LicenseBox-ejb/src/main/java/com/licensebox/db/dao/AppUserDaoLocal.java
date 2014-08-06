package com.licensebox.db.dao;

import com.licensebox.db.entity.AppRole.RoleName;
import com.licensebox.db.entity.AppUser;
import java.util.List;
import javax.ejb.Local;

/**
 * This is the interface for the AppUser DAO
 * 
 * @author Michael Paltsev & Anna Guzman
 */
@Local
public interface AppUserDaoLocal {

    /**
     * Returns a List of all the roles that the user has
     * @param username The username for whom we want to search
     * @return A List of all the RoleNames
     */
    List<RoleName> getUserRoles(String username);
    
    /**
     * Returns a List of AppUsers that have a role that matches to the RoleName
     * in the parameter
     * @param roleName The RoleName for which we want to get the List of AppUsers
     * @return A List of AppUsers or null if no user exists with such a Role
     */
    List<AppUser> getUserByRolename(RoleName roleName);

    /**
     * Returns the full name of a user
     * @param username The username for which we want to return the full name
     * @return A String in the following format: "First Name" "Last Name"
     * If a user with this username does not exist, an empty String is returned
     */
    String getUserFullName(String username);
    
    /**
     * Returns a List of AppUsers containing all the users in the application
     * @return A List of AppUsers containing all the users in the application. If
     * no users are found, a null object is returned
     */
    List<AppUser> getUserList();
    
    /**
     * Returns a AppUser that has this username
     *
     * @param username The username for whom we want to search
     * @return An AppUser that matches the username or a null object if such
     * a user does not exist
     */
    AppUser getUserByUsername(String username);

    /**
     * This method updates the Database with the user that is passed as the
     * parameter
     * @param userToUpdate The user that you want to update
     */
    void updateUser(AppUser userToUpdate);

    /**
     * Returns the team name to which the user with the username belongs
     * @param username The username of the user whose team name you want to find
     * @return The team name or an empty string if the user does not belong to any team
     * or the user was not found
     */
    String getUserTeamName(String username);

    /**
     * Adds the AppUser to the database
     * @param newUser The user that you want to add
     */
    void createNewUser(AppUser newUser);

    /**
     * Sets a password to the given user and stores it in the database
     * @param username The username of the user to whom you want to set the password
     * @param password A hashed password that you wish to set
     */
    void setUserPassword(String username, String password);
    
    /**
     * Checks if the user and the password are valid
     * @param username The username
     * @param password The password
     * @return True if the password belongs to the username and false otherwise
     */
    boolean isUserPasswordValid(String username, String password);
}