package com.licensebox.db.dao;

import com.licensebox.db.entity.License;
import com.licensebox.db.entity.LicenseHistory;
import java.io.Serializable;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * This DAO implements the LicenseHistoryDaoLocal Interface and acts as the DAO
 * of LicenseHistory Entity
 * 
 * @author Anna Guzman & Michael Paltsev
 */
@Stateless
public class LicenseHistoryDao implements LicenseHistoryDaoLocal, Serializable {
    
    @PersistenceContext(unitName = "LicenseBox-ejbPU")
    private EntityManager em;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void createHistoryEntry(LicenseHistory licenseHistory) {
        em.persist(licenseHistory);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void editHistoryEntry(LicenseHistory licenseHistory) {
        em.merge(licenseHistory);
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<LicenseHistory> getLicenseHistory(License license) {
        Query query = em.createNamedQuery("LicenseHistory.getByLicenseId");
        query.setParameter("licenseId", license.getLicenseId());
        List<LicenseHistory> retVal;
        
        try {
            retVal = query.getResultList();
        }
        catch (NoResultException ex){
            retVal = null;
        }
        return retVal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LicenseHistory> getAll() {
        Query query = em.createNamedQuery("LicenseHistory.getAll");
        List<LicenseHistory> retVal;
        
        try {
            retVal = query.getResultList();
        }
        catch (NoResultException ex){
            retVal = null;
        }
        return retVal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LicenseHistory> getLicenceHistoryByUsername(String username) {
        Query query = em.createNamedQuery("LicenseHistory.getByUsername");
        query.setParameter("username", username);
        List<LicenseHistory> retVal;
        
        try {
            retVal = query.getResultList();
        }         
        catch (NoResultException ex){
            retVal = null;
        }
        
        return retVal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LicenseHistory getLastFreeEntryByLicenseId(String licenseId) {
        LicenseHistory retVal;
        
        try {
            Query query = this.em.createNamedQuery("LicenseHistory.findLastFreeByLicenseId");
            query.setParameter("licenseId", licenseId);
            retVal = (LicenseHistory)query.getSingleResult();
        } catch (NoResultException ex) {
            retVal = null;
        }
        
        return retVal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LicenseHistory> getLicenseHistoryByLicenseId(String licenseId) {
        List<LicenseHistory> retVal;
        
        try {
            Query query = this.em.createNamedQuery("LicenseHistory.findByLicenseId");
            query.setParameter("licenseId", licenseId);
            retVal = query.getResultList();
        } catch (NoResultException ex) {
            retVal = null;
        }
        
        return retVal;
    }
       
}