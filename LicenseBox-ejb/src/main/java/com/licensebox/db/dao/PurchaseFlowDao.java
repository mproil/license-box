package com.licensebox.db.dao;

import com.licensebox.db.entity.PurchaseFlow;
import java.io.Serializable;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * PurchaseFlowDao implements the interface PurchaseFlowDaoLocal and acts as the
 * DAO of the PurchaseFlow entity
 *
 * @author Anna Guzman & Michael Paltsev
 */
@Stateless
public class PurchaseFlowDao implements PurchaseFlowDaoLocal, Serializable {
    
    @PersistenceContext(unitName = "LicenseBox-ejbPU")
    private EntityManager em;

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer createPurchaseFlow(PurchaseFlow purchaseFlow) {
        em.persist(purchaseFlow);
        em.flush();
        em.refresh(purchaseFlow);
        return purchaseFlow.getPurchaseId();
    }
        
    /**
     * {@inheritDoc}
     */
    @Override
    public void editPurchaseFlow(PurchaseFlow purchaseFlow) {
        em.merge(purchaseFlow);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PurchaseFlow getPurchaseFlow(Integer requestId) {
        Query query = em.createNamedQuery("PurchaseFlow.findByPurchaseId");
        query.setParameter("purchaseId", requestId);
        PurchaseFlow retVal;
        
        try {
            retVal = (PurchaseFlow)query.getSingleResult();
        } catch (NoResultException ex) {
            retVal = null;
        }
        return retVal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PurchaseFlow> getAll() {
        Query query = em.createNamedQuery("PurchaseFlow.findAll");
        List<PurchaseFlow> retVal;
        
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
    public List<PurchaseFlow> getForManagerApproval() {
        Query query = em.createNamedQuery("PurchaseFlow.findByManagerApproved");
        
        //only those requests approved by teamlead are waiting for license manager approval
        query.setParameter("managerApproved", Boolean.FALSE);        
        
        List<PurchaseFlow> retVal;
        
        try {
            retVal = query.getResultList();
        } catch (NoResultException ex) {
            retVal = null;
        }
        
        return retVal;
    }
   
}