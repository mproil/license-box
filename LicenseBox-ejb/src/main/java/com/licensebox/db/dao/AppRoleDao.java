package com.licensebox.db.dao;

import com.licensebox.db.entity.AppRole;
import java.io.Serializable;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * This DAO implements the AppRoleDaoLocal Interface
 * 
 * @author Michael Paltsev & Anna Guzman
 */
@Stateless
@LocalBean
public class AppRoleDao implements AppRoleDaoLocal, Serializable {
    
    // Serializable implementation
    private static final long serialVersionUID = 1L;
    
    @PersistenceContext(unitName = "LicenseBox-ejbPU")
    private EntityManager em;

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateRole(AppRole appRole) {
        em.merge(appRole);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public AppRole findRole(AppRole.RoleName roleName) {
        Query query = em.createNamedQuery("AppRole.findByRoleName");
        query.setParameter("roleName", roleName.getValue());
        AppRole ans;
        try {
            ans = (AppRole)query.getSingleResult();
        } catch (NoResultException ex) {
            ans = null;
        }
        return ans;
    }

}
