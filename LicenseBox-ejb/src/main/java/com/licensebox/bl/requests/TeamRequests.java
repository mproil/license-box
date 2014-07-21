package com.licensebox.bl.requests;

import com.licensebox.db.dao.AppRoleDaoLocal;
import com.licensebox.db.dao.AppUserDaoLocal;
import com.licensebox.db.dao.TeamDaoLocal;
import com.licensebox.db.entity.AppRole;
import com.licensebox.db.entity.AppUser;
import com.licensebox.db.entity.Team;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * This EJB is used for Team related requests.
 * From here you can switch team leaders, create a new team etc.
 * @author Michael Paltsev & Anna Guzman
 */
@Stateless
public class TeamRequests implements TeamRequestsLocal {

    //<editor-fold desc="EJB Injection" defaultstate="collapsed">
    @EJB
    TeamDaoLocal teamDao;
    
    @EJB
    AppUserDaoLocal appUserDao;
    
    @EJB
    AppRoleDaoLocal appRoleDao;
    //</editor-fold>
    
    /**
     * Method that is called after the construction of TeamRequests to inject
     * the beans.
     */
    @PostConstruct
    public void init() {
        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void switchTeamManager(Team team, String teamManagerToAdd) {
        
        String teamManagerToRemove = this.teamDao.getTeamById(team.getTeamId()).getTeamManager();
        team.setTeamManager(teamManagerToAdd);
        this.teamDao.updateTeam(team);
        
        if (needToRemoveTeamManagerRole(teamManagerToRemove)) {
            // Remove the team leader role from the user
            removeTeamManagerRole(teamManagerToRemove);
        }
        if (needToAddTeamManagerRole(teamManagerToAdd)) {
            // Add the team leader role to the user
            addTeamManagerRole(teamManagerToAdd);
        }
        
    }
    
    
    
    private void removeTeamManagerRole(String username) {
        AppUser user = this.appUserDao.getUserByUsername(username);
        AppRole appRole = this.appRoleDao.findRole(AppRole.RoleName.TEAM_LEADER);
        
        List<AppRole> appRoleList = user.getAppRoleList();
        appRoleList.remove(appRole);
        user.setAppRoleList(appRoleList);
        this.appUserDao.updateUser(user);
        
        List<AppUser> appUserList = appRole.getAppUserList();
        appUserList.remove(user);
        appRole.setAppUserList(appUserList);
        this.appRoleDao.updateRole(appRole);
    }
    
    private void addTeamManagerRole(String username) {
        AppUser user = this.appUserDao.getUserByUsername(username);
        AppRole appRole = this.appRoleDao.findRole(AppRole.RoleName.TEAM_LEADER);
        
        List<AppRole> appRoleList = user.getAppRoleList();
        appRoleList.add(appRole);
        user.setAppRoleList(appRoleList);
        this.appUserDao.updateUser(user);
    }

    private boolean needToRemoveTeamManagerRole(String teamManagerToRemove) {
        boolean ans = false;
        
        List<Team> managedTeams
                = this.teamDao.getTeamsByTeamManager(teamManagerToRemove);
        List<AppRole.RoleName> teamManagerRoles 
                = this.appUserDao.getUserRoles(teamManagerToRemove);
        boolean hasTeamLeaderRole 
                = teamManagerRoles.contains(AppRole.RoleName.TEAM_LEADER);
        
        if ((managedTeams == null) || (managedTeams.isEmpty())) {
            if (hasTeamLeaderRole) {
                ans = true;
            }
        } else {
            if ((managedTeams.size() == 1) && (hasTeamLeaderRole)) {
                ans = true;
            }
        }
        
        return ans;
        
    }
    
    private boolean needToAddTeamManagerRole(String teamManagerToAdd) {        
        List<AppRole.RoleName> teamManagerRoles 
                = this.appUserDao.getUserRoles(teamManagerToAdd);
        return !teamManagerRoles.contains(AppRole.RoleName.TEAM_LEADER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createNewTeam(Integer teamId, String teamName, String teamManager) {
        Team team = new Team();
        team.setTeamId(teamId);
        team.setTeamName(teamName);
        team.setTeamManager(teamManager);
        this.teamDao.createNewTeam(team);
        
        if (needToAddTeamManagerRole(teamManager)) {
            addTeamManagerRole(teamManager);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeTeam(Integer teamId) {
        Team team = this.teamDao.getTeamById(teamId);
        if (team != null) {
            List<AppUser> teamUsers = team.getAppUserList();
            if (teamUsers != null) {
                for (AppUser teamUser : teamUsers) {
                    teamUser.setTeam(null);
                }
            }
            String teamManagerUsername = team.getTeamManager();
            if ((teamManagerUsername != null) && (needToRemoveTeamManagerRole(teamManagerUsername))) {
                removeTeamManagerRole(teamManagerUsername);
            }
            this.teamDao.removeTeam(team);
        }
    }
    
}