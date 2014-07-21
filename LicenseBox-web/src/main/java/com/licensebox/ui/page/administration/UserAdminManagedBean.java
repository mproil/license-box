package com.licensebox.ui.page.administration;

import com.licensebox.bl.requests.CreateUserRequestLocal;
import com.licensebox.db.dao.AppRoleDaoLocal;
import com.licensebox.db.dao.TeamDaoLocal;
import com.licensebox.db.dao.AppUserDaoLocal;
import com.licensebox.db.entity.AppRole;
import com.licensebox.db.entity.AppUser;
import com.licensebox.db.entity.Team;
import com.licensebox.ui.Helper;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import org.apache.log4j.Logger;

/**
 * The Managed Bean is responsible for the Control of the User Administration
 * Page (the view).
 * 
 * @author Michael Paltsev & Anna Guzman
 */
@ManagedBean
@ViewScoped
public class UserAdminManagedBean implements Serializable {
    
    private static final Long serialVersionUID = 1L;
    
    //<editor-fold desc="EJB Injection" defaultstate="collapsed">
    @EJB
    private AppUserDaoLocal appUserDao;
    
    @EJB
    private TeamDaoLocal teamDao;
    
    @EJB
    private CreateUserRequestLocal createUserRequest;
    
    @EJB
    private AppRoleDaoLocal appRoleDao;
    //</editor-fold>
    
    //<editor-fold desc="Properties" defaultstate="collapsed">
    private List<AppUser> users;
    private List<AppUser> managers;
    private List<AppUser> licenseManagers;
    private AppUser selectedUser;
    private List<Team> teams;
    private String selectedUserTeam;
    private String managerUsername;
    private String licenseManagerUsername;
    
    // Properties for user creation
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Team team;
    private List<AppRole> roleList;
    
    private String currentUsername;
    
    private Logger logger = Logger.getLogger(UserAdminManagedBean.class);
    //</editor-fold>
    
    //<editor-fold desc="Getters & Setters" defaultstate="collapsed">
    
    public void setManagerUsername(String managerUsername) {
        this.managerUsername = managerUsername;
    }
    
    public String getManagerUsername() {
        return this.managerUsername;
    }
    
    public void setLicenseManagerUsername(String licenseManagerUsername) {
        this.licenseManagerUsername = licenseManagerUsername;
    }
    
    public String getLicenseManagerUsername() {
        return this.licenseManagerUsername;
    }
    
    /**
     * Get the all the users from the DB
     * 
     * @return A List of all the users
     */
    public List<AppUser> getUsers() {
        return this.users;
    }
    
    public List<AppUser> getManagers() {
        return this.managers;
    }
    
    public List<AppUser> getLicenseManagers() {
        return this.licenseManagers;
    }
    
    /**
     * Get the selected user from the table
     * @return The selected user from the table of users
     */
    public AppUser getSelectedUser() {
        return this.selectedUser;
    }
    
    /**
     * Sets the selected user in the user table
     * @param selectedUser 
     */
    public void setSelectedUser(AppUser selectedUser) {
        this.selectedUser = selectedUser;
    }
    
    /**
     * Get the value of teams
     *
     * @return the value of teams
     */
    public List<Team> getTeams() {
        return teams;
    }
    
    /**
     * Get the value of selectedUserTeam
     *
     * @return the value of selectedUserTeam
     */
    public String getSelectedUserTeam() {
        return selectedUserTeam;
    }
    
    /**
     * Set the value of selectedUserTeam
     *
     * @param selectedUserTeam new value of selectedUserTeam
     */
    public void setSelectedUserTeam(Integer teamId) {
        this.selectedUserTeam = (teamId != null) ? this.teamDao.getTeamById(teamId).getTeamName() : "";
    }    
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<AppRole> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<AppRole> roleList) {
        this.roleList = roleList;
    }
    
    public Team getTeam() {
        return this.team;
    }
    
    public void setTeam(Team team) {
        this.team = team;
    }
    //</editor-fold>
    
    /**
     * Creates a new instance of UserAdminManagedBean
     */
    public UserAdminManagedBean() {
        this.users = new LinkedList();
    }
    
    /**
     * An EJB initialization method.
     * You should not call it.
     */
    @PostConstruct
    public void init() {
        this.users = this.appUserDao.getUserList();
        this.teams = this.teamDao.getAllTeams();
        this.managers = this.appRoleDao.findRole(AppRole.RoleName.MANAGER).getAppUserList();
        this.licenseManagers = this.appRoleDao.findRole(AppRole.RoleName.LICENSE_MANAGER).getAppUserList();
        
        this.currentUsername = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
    }

    
    /**
     * This method saves the selected user
     */
    public void saveSelectedUser() {
        try {
            if (this.selectedUser != null) {
                this.appUserDao.updateUser(this.selectedUser);
                logger.info(this.currentUsername + " made changes to " + this.selectedUser);
                Helper.addFacesMessage(Helper.INFO, "The changes to the user " + this.selectedUser.getUsername()
                        + " were saved");
                this.selectedUser = null;
            }
        } catch (Exception ex) {
            Helper.addFacesMessage(Helper.ERROR, ex.getMessage());
        }
    }
    
    /**
     * This method creates a new user
     */
    public void createNewUser() {
        try {
            this.createUserRequest.createNewUser(this.username, this.firstName, this.lastName, this.email, this.team.getTeamId());
            logger.info(this.currentUsername + " created the new user " + this.username);
            Helper.addFacesMessage(Helper.INFO, "The user " + this.username + " was created");
            this.users = this.appUserDao.getUserList();
            this.username = null;
            this.firstName = null;
            this.lastName = null;
            this.email = null;
            this.team = null;
        } catch (Exception ex) {
            Helper.addFacesMessage(Helper.ERROR, ex.getMessage());
        }
    }
    
    /**
     * This method is used to validate the username. It checks to see if this username is
     * already in use
     * @param context
     * @param component
     * @param value
     * @throws ValidatorException 
     */
    public void validateUsername(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value instanceof String) {
            String tempUsername = (String)value;
            if (tempUsername.length() < 4) {
                throw new ValidatorException(
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Please use at least 4 characters for username"));
            }
            else if (isExistsUsername(tempUsername)) {
                throw new ValidatorException(
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "This username is already in use. Please enter another username"));
            }
        } else {
            throw new ValidatorException(
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Please enter a valid username"));
        }
    }
    
    private boolean isExistsUsername(String username) {
        return (this.appUserDao.getUserByUsername(username) != null) ? true : false;
    }

    /**
     * Resets the password for the selected user
     * @param selectedUser The user you want to reset the password to
     */
    public void resetPasswordForSelectedUser(AppUser selectedUser) {
        try {
            String tempUsername = selectedUser.getUsername();
            String fullName = String.format("%s %s", new Object[] {selectedUser.getFirstName(), selectedUser.getLastName()});
            String tempEmail = selectedUser.getEmail();
            this.createUserRequest.setNewPasswordForExistingUser(tempUsername, fullName, tempEmail);
            logger.info(this.currentUsername + " has reset the password of the user " + tempUsername);
            Helper.addFacesMessage(Helper.INFO, "Password for the user " + tempUsername + " was reset");
        } catch (Exception ex) {
            Helper.addFacesMessage(Helper.ERROR, ex.getMessage());
        }
    }
    
    /**
     * Sets a new Manager
     */
    public void setManager() {
        try {
            if (this.managerUsername != null) {
                this.createUserRequest.setNewManager(this.managerUsername);
                this.managers = this.appUserDao.getUserByRolename(AppRole.RoleName.MANAGER);
                this.users = this.appUserDao.getUserList();
                logger.info("The admin " + this.currentUsername + " had given the user " +
                        this.managerUsername + " the role of " + AppRole.RoleName.MANAGER.getValue());
                Helper.addFacesMessage(Helper.INFO, "The " + AppRole.RoleName.MANAGER.getValue() + " was set to "
                        + this.managerUsername);
            }
        } catch (Exception ex) {
            Helper.addFacesMessage(Helper.ERROR, ex.getMessage());
        }
    }
    
    /**
     * Removes a manager
     */
    public void removeManager() {
        try {
            if (this.managerUsername != null) {
                this.createUserRequest.removeManager(this.managerUsername);
                this.managers = this.appUserDao.getUserByRolename(AppRole.RoleName.MANAGER);
                this.users = this.appUserDao.getUserList();
                logger.info("The admin " + this.currentUsername + " removed the role " +
                    AppRole.RoleName.MANAGER.getValue() + " from the user " + this.managerUsername);
                Helper.addFacesMessage(Helper.INFO, "The " + AppRole.RoleName.MANAGER.getValue() + " was removed from "
                        + this.managerUsername);
            }
        } catch (Exception ex) {
            Helper.addFacesMessage(Helper.ERROR, ex.getMessage());
        }
    }
    
    /**
     * Sets a license manager
     */
    public void setLicenseManager() {
        try {
            if (this.licenseManagerUsername != null) {
                this.createUserRequest.setLicenseManager(this.licenseManagerUsername);
                this.licenseManagers = this.appUserDao.getUserByRolename(AppRole.RoleName.LICENSE_MANAGER);
                this.users = this.appUserDao.getUserList();
                logger.info("The admin " + this.currentUsername + " had given the user " +
                        this.licenseManagerUsername + " the role of " + AppRole.RoleName.LICENSE_MANAGER.getValue());
                Helper.addFacesMessage(Helper.INFO, "The " + AppRole.RoleName.LICENSE_MANAGER.getValue() + " was set to "
                        + this.licenseManagerUsername);
            }
        } catch (Exception ex) {
            Helper.addFacesMessage(Helper.ERROR, ex.getMessage());
        }
    }
    
    /**
     * Removes license manager
     */
    public void removeLicenseManager() {
        try {
            if (this.licenseManagerUsername != null) {
                this.createUserRequest.removeLicenseManager(this.licenseManagerUsername);
                this.licenseManagers = this.appUserDao.getUserByRolename(AppRole.RoleName.LICENSE_MANAGER);
                this.users = this.appUserDao.getUserList();
                logger.info("The admin " + this.currentUsername + " removed the role " +
                    AppRole.RoleName.LICENSE_MANAGER.getValue() + " from the user " + this.licenseManagerUsername);
                Helper.addFacesMessage(Helper.INFO, "The " + AppRole.RoleName.LICENSE_MANAGER.getValue() + " was removed from "
                        + this.licenseManagerUsername);
            }
        } catch (Exception ex) {
            Helper.addFacesMessage(Helper.ERROR, ex.getMessage());
        }
    }

    /**
     * This method is used to check if the user is disabled. E.g. that he does
     * not belong to any role.
     * 
     * @param username The username we want to check
     * @return True if the user is disabled and false otherwise
     */
    public boolean isUserDisabled(String username) {
        boolean retVal = true;
        if (username != null) {
            AppUser appUser = this.appUserDao.getUserByUsername(username);
            if (appUser != null) {
                List<AppRole> appRoles = appUser.getAppRoleList();
                if (!appRoles.isEmpty()) {
                    retVal = false;
                }
            }
        }
        return retVal;
    }
    
    /**
     * This method disables the selected user
     */
    public void disableUser() {
        try {
            if (this.selectedUser != null) {
                String tempUsername = this.selectedUser.getUsername();
                this.createUserRequest.disableUser(tempUsername);
                Helper.addFacesMessage(Helper.INFO, "The user " + tempUsername + " is now disabled");
                logger.info("The admin " + this.currentUsername + " disabled user " + tempUsername);
                this.selectedUser = null;
                this.users = this.appUserDao.getUserList();
            }
        } catch (Exception ex) {
            Helper.addFacesMessage(Helper.ERROR, ex.getMessage());
        }
    }
    
    /**
     * This method enables the selected user
     */
    public void enableUser() {
        try {
            if (this.selectedUser != null) {
                String tempUsername = this.selectedUser.getUsername();
                this.createUserRequest.enableUser(tempUsername);
                Helper.addFacesMessage(Helper.INFO, "The user " + tempUsername + " is now enabled");
                logger.info("The admin " + this.currentUsername + " enabled user " + tempUsername);
                this.selectedUser = null;
                this.users = this.appUserDao.getUserList();
            }
        } catch (Exception ex) {
            Helper.addFacesMessage(Helper.ERROR, ex.getMessage());
        }
    }
   
}