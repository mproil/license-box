package com.licensebox.ui.session;


import com.licensebox.db.dao.AppUserDaoLocal;
import com.licensebox.db.entity.AppRole.RoleName;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;

/**
 * This Managed Bean is used throughout the session of the user
 * 
 * @author Michael Paltsev & Anna Guzman
 */
@ManagedBean
@SessionScoped
public class UserSessionManagedBean implements Serializable {

    
    @EJB
    private AppUserDaoLocal appUserDao;

    private String username;

    /**
     * Get the value of username
     *
     * @return the value of username
     */
    public String getUsername() {
        return username;
    }
    
    private Logger logger = Logger.getLogger(UserSessionManagedBean.class);
    
    private String fullname;

    /**
     * Get the value of fullname
     *
     * @return the value of fullname
     */
    public String getFullname() {
        return (this.fullname != null) ? this.fullname : (this.fullname = this.appUserDao.getUserFullName(this.username));
    }
    
    private List<RoleName> roles;

    
    
    /**
     * Creates a new instance of UserSessionManagedBean
     */
    public UserSessionManagedBean() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        this.username = externalContext.getRemoteUser();
        logger.info(username + " has logged in to LicenseBox");
    }
    
    @PostConstruct
    public void init() {
        // We should already have the username from the constructor
        this.roles = this.appUserDao.getUserRoles(this.username);
    }
    
    /**
     * Returns true if the user is an Administrator and false otherwise
     * @return True if the user is an Administrator and false otherwise
     */
    public boolean isAdministrator() {
        return this.roles.contains(RoleName.ADMIN);
    }
    
    /**
     * Returns true if the user is a License Administrator and false otherwise
     * @return True if the user is a License Administrator and false otherwise
     */
    public boolean isLicenseAdmin() {
        return this.roles.contains(RoleName.LICENSE_MANAGER);
    }
    
    /**
     * Returns true if the user is a Manager and false otherwise
     * @return True if the user is a Manager and false otherwise
     */
    public boolean isManager() {
        return this.roles.contains(RoleName.MANAGER);
    }
    
    /**
     * Returns true if the user is a Team Leader and false otherwise
     * @return True if the user is a Team Leader and false otherwise
     */
    public boolean isTeamLeader() {
        return this.roles.contains(RoleName.TEAM_LEADER);
    }
}
