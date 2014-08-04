package com.licensebox.db.dao;

import com.licensebox.db.entity.AppRole;
import com.licensebox.db.entity.AppRole.RoleName;
import com.licensebox.db.entity.AppUser;
import com.licensebox.db.entity.Team;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * This DAO implements the AppUserDaoLocal Interface and is used as the DAO of
 * the entity AppUser
 *  
 * @author Michael Paltsev & Anna Guzman
 */
@Stateless
public class AppUserDao implements AppUserDaoLocal, Serializable {
    
    // Serializable implementation
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "LicenseBox-ejbPU")
    private EntityManager em;
        
    /**
     * {@inheritDoc}
     */
    @Override
    public AppUser getUserByUsername(String username) {
        Query query = em.createNamedQuery("AppUser.findByUsername");
        query.setParameter("username", username);
        AppUser retVal;
        
        try {
            retVal = (AppUser)query.getSingleResult();
        } catch (NoResultException ex) {
            retVal = null;
        }
        
        return retVal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RoleName> getUserRoles(String username) {
        List<RoleName> retVal = new LinkedList();
        
        AppUser tempUser = this.getUserByUsername(username);
        try {
            List<AppRole> tempRoleList = tempUser.getAppRoleList();
            for (AppRole tempRole : tempRoleList) {
                retVal.add(tempRole.getRoleName());
            }
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
    public String getUserFullName(String username) {
        String retVal;
        
        AppUser tempUser = this.getUserByUsername(username);
        if (tempUser != null) {
            retVal = String.format("%s %s", new Object[] {tempUser.getFirstName(), tempUser.getLastName()});
        } else {
            retVal = "";
        }
        
        return retVal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AppUser> getUserList() {
        Query query = em.createNamedQuery("AppUser.findAll");
        List<AppUser> retVal;
        
        try {
            retVal = (List<AppUser>)query.getResultList();
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
    public void updateUser(AppUser userToUpdate) {
        em.merge(userToUpdate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserTeamName(String username) {
        String retVal;
        AppUser user = this.getUserByUsername(username);
        Team team;
        
        if ((user != null) && ((team = user.getTeam()) != null)) {
            retVal = team.getTeamName();
        } else {
            retVal = "";
        }
        
        return retVal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createNewUser(AppUser newUser) {
        em.persist(newUser);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUserPassword(String username, String password) {
        AppUser user = this.getUserByUsername(username);
        if (user != null) {
            user.setPassword(password);
        }
        em.merge(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AppUser> getUserByRolename(RoleName roleName) {
        
        List<AppUser> ans;
        
        Query query = em.createNamedQuery("AppUser.findUserByRoleName");
        query.setParameter("roleName", roleName.getValue());        
        
        try {
            ans = (List<AppUser>)query.getResultList();
        }
        catch (NoResultException ex){            
            ans=null;
        }
        return ans;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isUserPasswordValid(String username, String password) {
        
        AppUser user;
        boolean retVal;
        
        Query query = em.createNamedQuery("AppUser.findByUsername");
        query.setParameter("username", username);
        
        try {
            List<AppUser> listOfUsers = query.getResultList();
            
            if ((listOfUsers == null) || (listOfUsers.size() != 1)){
                retVal = false;
            } else {
                user = listOfUsers.get(0);
                retVal = user.getPassword().equals(password);
            }
        } catch (NoResultException ex) {
            retVal = false;
        }
        
        return retVal;
    }
    
    
    
}
