package com.licensebox.ui.page;

import com.licensebox.bl.license.LicenseManagerLocal;
import com.licensebox.db.dao.AppUserDaoLocal;
import com.licensebox.db.dao.LicenseDaoLocal;
import com.licensebox.db.dao.LicenseFlowDaoLocal;
import com.licensebox.db.dao.ProgramDaoLocal;
import com.licensebox.db.dao.TeamDaoLocal;
import com.licensebox.db.entity.AppRole;
import com.licensebox.db.entity.AppUser;
import com.licensebox.db.entity.License;
import com.licensebox.db.entity.LicenseFlow;
import com.licensebox.db.entity.Program;
import com.licensebox.db.entity.Team;
import com.licensebox.ui.Helper;
import com.licensebox.ui.session.UserSessionManagedBean;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;

/**
 * This managed bean is responsible for controlling the index.xhtml page
 * 
 * @author Michael Paltsev & Anna Guzman
 */
@ManagedBean
@ViewScoped
public class DashboardManagedBean implements Serializable {

    //<editor-fold desc="EJB Injection" defaultstate="collapsed">
    @EJB
    private TeamDaoLocal teamDao;
    
    @EJB
    private AppUserDaoLocal appUserDao;
    
    @EJB
    private LicenseFlowDaoLocal licenseFlowDao;
    
    @EJB
    private ProgramDaoLocal programDao;
    
    @EJB
    private LicenseManagerLocal licenseManager;
    
    @EJB
    private LicenseDaoLocal licenseDao;
    
    @ManagedProperty("#{userSessionManagedBean}")
    private UserSessionManagedBean userSessionManagedBean;
    //</editor-fold>
    
    //<editor-fold desc="Properties" defaultstate="collapsed">
    private Logger logger = Logger.getLogger(DashboardManagedBean.class);
    
    private List<License> licenses;
    private License selectedLicense;
    private String teamName;
    private String teamManager;
    private String teamManagerEmail;
    private String adminEmail;
    
    private List<LicenseFlow> licenseFlows;
    private List<Program> programs;
    private Program program;
    
    private String username;
    //</editor-fold>
    
    //<editor-fold desc="Getters & Setters" defaultstate="collapsed">
    public String getTeamName() {
        return this.teamName;
    }
    
    public String getTeamManager() {
        return this.teamManager;
    }
    
    public String getTeamManagerEmail() {
        return this.teamManagerEmail;
    }
    
    public String getAdminEmail() {
        return this.adminEmail;
    }
    
    public void setUserSessionManagedBean(UserSessionManagedBean userSessionManagedBean) {
        this.userSessionManagedBean = userSessionManagedBean;
    }

    public List<License> getLicenses() {
        return licenses;
    }

    public License getSelectedLicense() {
        return selectedLicense;
    }

    public void setSelectedLicense(License selectedLicense) {
        this.selectedLicense = selectedLicense;
    }
    
    public List<LicenseFlow> getLicenseFlows() {
        return this.licenseFlows;
    }
    
    public List<Program> getPrograms() {
        return programs;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }
    //</editor-fold>
    
    
    /**
     * This method is called after the constructor of this class to enable
     * the injection of the EJBs
     */
    @PostConstruct
    public void init() {
        this.username = this.userSessionManagedBean.getUsername();
        this.teamName = this.appUserDao.getUserTeamName(username);
        if (!this.teamName.isEmpty()) {
            Team team = this.teamDao.getTeamByName(this.teamName);
            String teamManagerUsername = team.getTeamManager();
            AppUser teamManagerUser = this.appUserDao.getUserByUsername(teamManagerUsername);
            this.teamManager = String.format("%s %s", new Object[] {teamManagerUser.getFirstName(), 
                teamManagerUser.getLastName()});
            this.teamManagerEmail = teamManagerUser.getEmail();
        }
        this.adminEmail = this.appUserDao.getUserByRolename(AppRole.RoleName.ADMIN).get(0).getEmail();
        AppUser appUser = this.appUserDao.getUserByUsername(this.username);
        this.licenses = this.licenseDao.getLicensesByAppUser(appUser);
        this.licenseFlows = this.licenseFlowDao.getByUsername(this.username);
        this.programs = this.programDao.getAll();
    }
    
    /**
     * This method is used to return a license that was selected from the table of licenses
     */
    public void returnSelectedLicense() {
        if (this.selectedLicense != null) {
            try {
                this.licenseManager.returnLicense(this.selectedLicense.getLicenseId());
                AppUser appUser = this.appUserDao.getUserByUsername(this.username);
                this.licenses = this.licenseDao.getLicensesByAppUser(appUser);
                logger.info(this.username + " returned the license " + this.selectedLicense.getLicenseId());
                Helper.addFacesMessage(Helper.INFO, "You've returned the license " + this.selectedLicense.getLicenseId());
                this.selectedLicense = null;
            } catch (Exception ex) {
                Helper.addFacesMessage(Helper.ERROR, ex.getMessage());
                logger.error("An error occurred when user " + this.username + " tried to return license " + this.selectedLicense.getLicenseId());
                logger.error(ex.getMessage(), ex);
            }
        } else {
            Helper.addFacesMessage(Helper.ERROR, "Couldn't get the selected license.\n"
                    + "Please refresh the page and try again.");

        }
    }
    
    /**
     * This method creates a new license request for the user
     */
    public void createNewLicenseRequest() {
        if (this.program != null) {
            try {
                this.licenseManager.createNewLicenseRequest(this.username, this.program.getProgramId());
                this.licenseFlows = this.licenseFlowDao.getByUsername(this.username);
                logger.info(this.username + " asked for a new license for program "
                        + this.program.getProgramName() + " " + this.program.getVersion());
                Helper.addFacesMessage(Helper.INFO, "You've asked for a new license for program "
                        + this.program.getProgramName() + " " + this.program.getVersion());
                this.program = null;
            } catch (Exception ex) {
                Helper.addFacesMessage(Helper.ERROR, ex.getMessage());
                logger.error(this.username + " failed to create a new license request");
                logger.error(ex.getMessage(), ex);
            }
        } else {
            Helper.addFacesMessage(Helper.ERROR, "Couldn't get the selected program.\n"
                    + "Please refresh the page and try again.");
        }
    }
}