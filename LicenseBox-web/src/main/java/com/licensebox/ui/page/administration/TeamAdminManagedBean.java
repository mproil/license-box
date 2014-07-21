package com.licensebox.ui.page.administration;

import com.licensebox.bl.requests.TeamRequestsLocal;
import com.licensebox.db.dao.AppUserDaoLocal;
import com.licensebox.db.dao.TeamDaoLocal;
import com.licensebox.db.entity.AppUser;
import com.licensebox.db.entity.Team;
import com.licensebox.ui.Helper;
import java.io.Serializable;
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
 * A Managed Bean for the Team Management page
 * 
 * @author Michael Paltsev & Anna Guzman
 */
@ManagedBean
@ViewScoped
public class TeamAdminManagedBean implements Serializable {
    
    private static final Long serialVersionUID = 1L;
    
    //<editor-fold desc="EJB Injection" defaultstate="collapsed">
    @EJB
    private TeamDaoLocal teamDao;
    
    @EJB
    private AppUserDaoLocal appUserDao;
    
    @EJB
    private TeamRequestsLocal teamRequest;
    //</editor-fold>
    
    //<editor-fold desc="Properties" defaultstate="collapsed">
    private List<Team> teams;
    private List<AppUser> users;
    private Team selectedTeam;

    private Integer teamId;
    private String teamName;
    private String teamManager;
    
    private String username;
    
    private Logger logger = Logger.getLogger(TeamAdminManagedBean.class);
    //</editor-fold>

    //<editor-fold desc="Getters & Setters" defaultstate="collapsed">
    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamManager() {
        return teamManager;
    }

    public void setTeamManager(String teamManager) {
        this.teamManager = teamManager;
    }
    
    public List<Team> getTeams() {
        return this.teams;
    }
    
    public Team getSelectedTeam() {
        return this.selectedTeam;
    }
    
    public void setSelectedTeam(Team selectedTeam) {
        this.selectedTeam = selectedTeam;
    }
    
    
    public List<AppUser> getUsers() {
        return this.users;
    }
    //</editor-fold>

    /**
     * Used to initialize the teams and users list after the injection
     * of the EJB's is done.
     */
    @PostConstruct
    public void init() {
        this.username = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        this.teams = this.teamDao.getAllTeams();
        this.users = this.appUserDao.getUserList();
    }
    
    /**
     * Saves the currently selected team into the database
     */
    public void saveSelectedTeam() {
        if (this.selectedTeam != null) {
            try {
                // Get the current team manager and check if we need to switch team managers
                String currentTeamManager = this.teamDao.getTeamById(this.selectedTeam.getTeamId()).getTeamManager();
                if (!currentTeamManager.equals(this.selectedTeam.getTeamManager())) {
                    this.teamRequest.switchTeamManager(selectedTeam, this.selectedTeam.getTeamManager());
                } else {
                    this.teamDao.updateTeam(this.selectedTeam);
                }
                logger.info(this.username + " updated the team " + this.selectedTeam.getTeamName());
                Helper.addFacesMessage(Helper.INFO, "The team " + this.selectedTeam.getTeamName() + " was updated");
                this.selectedTeam = null;
            } catch (Exception ex) {
                Helper.addFacesMessage(Helper.ERROR, ex.getMessage());
            }
        }
    }
    
    /**
     * Creates a new team
     */
    public void createNewTeam() {
        try {
            this.teamRequest.createNewTeam(this.teamId, this.teamName, this.teamManager);
            this.teams = this.teamDao.getAllTeams();
            logger.info(this.username + " created a new team " + this.teamName);
            Helper.addFacesMessage(Helper.INFO, "The team " + this.teamName + " was created.");
            this.teamId = null;
            this.teamName = null;
            this.teamManager = null;
        } catch (Exception ex) {
            Helper.addFacesMessage(Helper.ERROR, ex.getMessage());
        }
    }
    
    /**
     * This method is being used to validate the team id. It checks if this team id is not in use
     * @param context
     * @param component
     * @param value
     * @throws ValidatorException 
     */
    public void validateTeamId(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        try {
            if (isExistsTeamId((Integer)value)) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                        "Error", "This team id already exists, please enter another team id"));
            }
        } catch (Exception ex) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error", "An unkown error occured. Please try again"));
        }
    }
    
    /**
     * Checks if a team with this id already exists
     * @param teamId The team id to check
     * @return True if the team exists and false otherwise
     */
    private boolean isExistsTeamId(Integer teamId) {
        return (this.teamDao.getTeamById(teamId) != null) ? true : false;
    }

    /**
     * Deletes the selected team from the team table
     */
    public void deleteTeam() {
        if (this.selectedTeam != null) {
            try {
                this.teamRequest.removeTeam(this.selectedTeam.getTeamId());
                this.teams = this.teamDao.getAllTeams();
                logger.info(this.username + " deleted the team " + this.selectedTeam.getTeamName());
                Helper.addFacesMessage(Helper.INFO, "The team " + this.selectedTeam.getTeamName() + " has been deleted");
                this.selectedTeam = null;
            } catch (Exception ex) {
                Helper.addFacesMessage(Helper.ERROR, ex.getMessage());
            }
        }
    }
}