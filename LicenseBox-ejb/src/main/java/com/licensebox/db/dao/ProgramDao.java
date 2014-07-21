package com.licensebox.db.dao;

import com.licensebox.db.entity.Program;
import java.io.Serializable;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * ProgramDao implements the interface ProgramDaoLocal
 *
 * @author Anna Guzman & Michael Paltsev
 */
@Stateless
public class ProgramDao implements ProgramDaoLocal, Serializable {
    
    @PersistenceContext(unitName = "LicenseBox-ejbPU")
    private EntityManager em;

    /**
     * {@inheritDoc}
     */
    @Override
    public void createProgram(Program program) {
        em.persist(program);
    }

    /**
     * {@inheritDoc}
     */    
    @Override
    public void editProgram(Program program) {
        em.merge(program);
    }

    /**
     * {@inheritDoc}
     */    
    @Override
    public Program getByProgramId(Integer programId) {
        Query query = em.createNamedQuery("Program.findByProgramId");
        query.setParameter("programId", programId);
        Program retVal;
        
        try {
            retVal = (Program)query.getSingleResult();
        } catch (NoResultException ex) {
            retVal = null;
        }
        
        return retVal;
    }

    /**
     * {@inheritDoc}
     */    
    @Override
    public List<Program> getAll() {
        Query query = em.createNamedQuery("Program.findAll");
        List<Program> retVal;
        
        try {
            retVal = query.getResultList();
        } catch (NoResultException ex) {
            retVal = null;
        }
        return retVal;
    }
 
}