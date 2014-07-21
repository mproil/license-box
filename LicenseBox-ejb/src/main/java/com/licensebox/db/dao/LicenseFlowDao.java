package com.licensebox.db.dao;

import com.licensebox.db.entity.LicenseFlow;
import com.licensebox.db.entity.Team;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * This DAO implements the LicenseFlowDaoLocal Interface and is used as the DAO
 * of the LicenseFlow entity.
 * 
 * @author Anna Guzman & Michael Paltsev
 */
@Stateless
public class LicenseFlowDao implements LicenseFlowDaoLocal, Serializable {
    
    @PersistenceContext(unitName = "LicenseBox-ejbPU")
    private EntityManager em;

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer createRequest(LicenseFlow licenseFlow) {
        em.persist(licenseFlow);
        em.flush();
        em.refresh(licenseFlow);
        return licenseFlow.getRequestId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void editRequest(LicenseFlow licenseFlow) {
        em.merge(licenseFlow);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public LicenseFlow getByRequestId (Integer requestId){
        Query query = em.createNamedQuery("LicenseFlow.findByRequestId");
        query.setParameter("requestId", requestId);
        LicenseFlow ans;
        try {
            ans = (LicenseFlow)query.getSingleResult();
        }
        catch (NoResultException ex) {
            ans = null;
        }
        return ans;
    }
    
    /**
     * {@inheritDoc}
     */    
    @Override
    public List<LicenseFlow> getByUsername(String username){
        Query query = em.createNamedQuery("LicenseFlow.findByUsername");
        query.setParameter("username", username);
        List<LicenseFlow> ans;        
        try {
            ans = query.getResultList();
        }
        catch (NoResultException ex){
            ans = null;
        }
        return ans;
    }

    /**
     * {@inheritDoc}
     */    
    @Override
    public List<LicenseFlow> getForLicmanApproval() {
        Query query = em.createNamedQuery("LicenseFlow.findByLicmanApproved");
        
        //only those requests approved by teamlead are waiting for license manager approval
        query.setParameter("licmanApproved", false).setParameter("teamleadApproved", true);        
        
        List<LicenseFlow> ans;
        try {
            ans = query.getResultList();
        }
        catch (NoResultException ex){
            ans = null;
        }
        return ans;
       
        
    }

    /**
     * {@inheritDoc}
     */    
    @Override
    public List<LicenseFlow> getForTeamleadApproval(String teamleadName){
        
        List<LicenseFlow> resFlowList = new ArrayList<>();
        List<LicenseFlow> tempFlowList;
        
        //Select all requests that are waiting for any team leader's approval
        Query query = em.createNamedQuery("LicenseFlow.findByTeamleadApproved");
        query.setParameter("teamleadApproved", Boolean.FALSE);
        
        try {
            tempFlowList = query.getResultList();
        }
        catch (NoResultException ex){
            tempFlowList = null;
            return tempFlowList;
        }        
        
        //iterate through the list and save only those requests that are waiting for
        //specified team leader's approval
        
        for (LicenseFlow tempFlow : tempFlowList){
            
            if (!getTeamleadForRequest(tempFlow.getRequestId()).equals(teamleadName)){
            } else {
                resFlowList.add(tempFlow);
            }            
        }        
        if (resFlowList.isEmpty()) {
            resFlowList = null;
        }
        
        return resFlowList;
    }
    
    /**
     * {@inheritDoc}
     */    
    @Override
    public String getTeamleadForRequest (Integer requestId){
        
        LicenseFlow tempFlow = getByRequestId(requestId);
        Team team = tempFlow.getAppUser().getTeam();
        String teamleadName = (team != null) ? team.getTeamManager() : "";
        
        return teamleadName;
        
    }
}
