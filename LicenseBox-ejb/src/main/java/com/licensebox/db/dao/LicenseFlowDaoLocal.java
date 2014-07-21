package com.licensebox.db.dao;

import com.licensebox.db.entity.LicenseFlow;
import java.util.List;
import javax.ejb.Local;

/**
 * This Interface defines the methods in the LicenseFlow DAO
 * 
 * @author Anna Guzman & Michael Paltsev
 */
@Local
public interface LicenseFlowDaoLocal {

    /**
     * Creates a new License Request in the database
     * @param licenseFlow The license request that you want to create
     * @return A license request id that was generated automatically
     */
    Integer createRequest(LicenseFlow licenseFlow);

    /**
     * Updates the request in the database
     * @param licenseFlow The license request that you want to update in the database
     */
    void editRequest(LicenseFlow licenseFlow);
    
    /**
     * Find a license request by a request id
     * @param requestId The request id that belongs the license request
     * @return A license request or a null object if no license request is found
     */
    LicenseFlow getByRequestId(Integer requestId);

    /**
     * Finds a list of all the license requests that were created by a specific
     * user
     * @param username The username that belongs to that specific user
     * @return A List of license requests or a null object if none are found
     */
    List<LicenseFlow> getByUsername(String username);
    
    /**
     * Returns all the license requests that are awaiting the license manager
     * approval
     * @return A List of license requests or a null object if none are found
     */
    List<LicenseFlow> getForLicmanApproval();
    
    /**
     * Returns all the license requests that are awaiting the team leader
     * approval
     * @return A List of license requests or a null object if none are found
     */
    List<LicenseFlow> getForTeamleadApproval(String teamleadName);
    
    /**
     * Returns the team leader that should approve this request
     * @param requestId The request id for which we want to find the team leader
     * @return A username of the team leader or an empty string if none exists
     */
    String getTeamleadForRequest (Integer requestId);

}