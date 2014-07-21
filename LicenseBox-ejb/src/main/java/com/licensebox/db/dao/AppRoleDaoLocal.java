package com.licensebox.db.dao;

import com.licensebox.db.entity.AppRole;
import com.licensebox.db.entity.AppRole.RoleName;
import javax.ejb.Local;

/**
 * This is the interface for the AppRole DAO
 * 
 * @author Anna Guzman & Michael Paltsev
 */
@Local
public interface AppRoleDaoLocal {

    /**
     * Updates the role in the database
     * @param appRole The role that you want to update
     */
    void updateRole(AppRole appRole);
    
    /**
     * Returns a Role which RoleName matches the one that is given as a parameter
     * @param roleName The RoleName for which you want to find the Role
     * @return A Role that matches the parameter and null if no Role is found
     */
    AppRole findRole(RoleName roleName);
}
