package com.licensebox.db.dao;

import com.licensebox.db.entity.Team;
import java.util.List;
import javax.ejb.Local;

/**
 * This Interface defines the methods that will be used by the Team DAO
 * 
 * @author Michael Paltsev & Anna Guzman
 */
@Local
public interface TeamDaoLocal {

    /**
     * Finds a Team with a specific id
     * @param teamId The team id you want to search for
     * @return A Team object or a null object if none found
     */
    public Team getTeamById(int teamId);

    /**
     * Returns a List of all the teams in the database
     * @return A List of Team objects or null if none found
     */
    List<Team> getAllTeams();

    /**
     * Finds a team by the team name
     * @param teamName The team name of the team you want to find
     * @return A Team or a null object if none found
     */
    Team getTeamByName(String teamName);

    /**
     * Returns a list of teams that are managed by a specific user
     * @param teamManager Team manager whose teams you wish to find
     * @return A List of Team objects or null if no team is found
     */
    List<Team> getTeamsByTeamManager(String teamManager);

    /**
     * Updates a team in the database
     * @param team The team that you want to update
     */
    void updateTeam(Team team);
    
    /**
     * Creates a new team in the database
     * @param team The team that you want to create
     */
    void createNewTeam(Team team);

    /**
     * Deletes a team from the database
     * @param team The team that you want to delete
     */
    void removeTeam(Team team);
}
