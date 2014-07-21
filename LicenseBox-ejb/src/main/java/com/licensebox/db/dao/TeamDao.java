package com.licensebox.db.dao;

import com.licensebox.db.entity.Team;
import java.io.Serializable;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Michael Paltsev & Anna Guzman
 */
@Stateless
public class TeamDao implements TeamDaoLocal, Serializable {
    
    // Serializable implementation
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "LicenseBox-ejbPU")
    private EntityManager em;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Team getTeamById(int teamId) {
        Query query = em.createNamedQuery("Team.findByTeamId");
        query.setParameter("teamId", new Integer(teamId));
        Team retVal;
        
        try {
            retVal = (Team)query.getSingleResult();
        } catch (NoResultException ex){
            retVal = null;
        }
        return retVal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Team> getAllTeams() {
        Query query = em.createNamedQuery("Team.findAll");
        List<Team> retVal;
        
        try {
            retVal = query.getResultList();
        } catch (NoResultException ex) {
            retVal = null;
        }
        
        return retVal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Team getTeamByName(String teamName) {
        Query query = em.createNamedQuery("Team.findByTeamName");
        query.setParameter("teamName", teamName);
        Team retVal;
        
        try {
            retVal = (Team)query.getSingleResult();
        } catch (NoResultException ex) {
            retVal = null;
        }
        
        return retVal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Team> getTeamsByTeamManager(String teamManager) {
        Query query = em.createNamedQuery("Team.findByTeamManager");
        query.setParameter("teamManager", teamManager);
        
        List<Team> retVal;
        try {
            retVal = query.getResultList();
        } catch (NoResultException ex) {
            retVal = null;
        }
        return retVal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTeam(Team team) {
        em.merge(team);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void createNewTeam(Team team) {
        em.persist(team);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeTeam(Team team) {
        em.remove(team);
    }
       
}