package com.licensebox.db.dao;

import com.licensebox.db.entity.AppUser;
import com.licensebox.db.entity.License;
import com.licensebox.db.entity.Program;
import java.io.Serializable;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * This DAO implements the LicenseDaoLocal Interface
 *
 * @author Anna Guzman & Michael Paltsev
 */
@Stateless
public class LicenseDao implements LicenseDaoLocal, Serializable {
    
    @PersistenceContext(unitName = "LicenseBox-ejbPU")
    private EntityManager em;

    /**
     * {@inheritDoc}
     */
    @Override
    public void createNewLicene(License license) {
        em.persist(license);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLicense(License license) {
        em.merge(license);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public License getLicenseById(String licenseId) {
        Query query = em.createNamedQuery("License.findByLicenseId");
        query.setParameter("licenseId", licenseId);
        License retVal;
        try {
            retVal = (License)query.getSingleResult();
        }
        catch (NoResultException ex){
            retVal = null;
        }
        
        return retVal;
    }

    /**
     * {@inheritDoc}
     *
     */
    
    @Override
    public List<License> getLicensesByProgram(Program program) {
        Query query = em.createNamedQuery("License.findByProgram");
        query.setParameter("program", program);
        List<License> retVal;
        
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
    public List<License> getFreeLicensesByProgram(Program program) {
        Query query = em.createNamedQuery("License.findFreeByProgram");
        query.setParameter("program", program);
        List<License> retVal;
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
    public List<License> getAll() {
        Query query = em.createNamedQuery("License.findAll");
        List<License> retVal;
        try{
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
    public List<License> getLicensesByAppUser(AppUser appUser) {
        List<License> retVal;
        
        try {
            Query query = em.createNamedQuery("License.findByUser");
            query.setParameter("appUser", appUser);
            retVal = query.getResultList();
        } catch (NoResultException ex) {
            retVal = null;
        }
        
        return retVal;
    }

}