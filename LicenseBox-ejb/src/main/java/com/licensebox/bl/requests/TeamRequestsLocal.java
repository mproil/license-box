package com.licensebox.bl.requests;

import com.licensebox.db.entity.Team;
import javax.ejb.Local;

/**
 * This interface declares the methods that are used by the application administrator
 * regarding the teams in the application
 * 
 * @author Michael Paltsev & Anna Guzman
 */
@Local
public interface TeamRequestsLocal {

    /**
     * This method adds a new team manager to the team and removes the previous
     * one. It also adds the team manager role to the new team manager (if needed)
     * and removes the team manager role from the previous team manager (if needed)
     * @param team The team to which you want to add the team manager
     * @param teamManagerToAdd The team managers username that you want to add
     */
    void switchTeamManager(Team team, String teamManagerToAdd);

    /**
     * This method creates a new team in the database and also adds the necessary 
     * roles to the new team manager (if needed)
     * @param teamId The team id of the new team
     * @param teamName The team name of the new team
     * @param teamManager The username of the team manager
     */
    void createNewTeam(Integer teamId, String teamName, String teamManager);

    /**
     * This method is responsible for deleting a team from the database.
     * It also removes all the team members from it, removes the team manager
     * and updates his roles accordingly.
     * @param teamId The team id that you want to remove
     */
    void removeTeam(Integer teamId);
    
}