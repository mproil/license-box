package com.licensebox.bl.license;

import com.licensebox.db.entity.AppUser;
import com.licensebox.db.entity.LicenseHistory;
import com.licensebox.db.entity.Program;
import com.licensebox.db.entity.PurchaseFlow;
import javax.ejb.Local;

/**
 *
 * @author Anna Guzman & Michael Paltsev
 */
@Local
public interface LicenseManagerLocal {
    
    /**
     * Creates new license request
     * @param username The username that requests new license
     * @param programId The program id for which the license is requested
     * @return A new license request id
     */
    Integer createNewLicenseRequest(String username, Integer programId);
    
    /**
     * Approves a request by the team manager
     * @param requestId The request id that you want to approve
     * @param teamLeader The teamLeader username that approves this request
     * @throws Exception if team leader is not allowed to approve the request or if
     * the request is already approved by team leader or license manager
     */
    void approveByTeamLeader (Integer requestId, String teamLeader) throws Exception;
    
    /**
     * Approves a request by the license manager
     * @param requestId The request id that you want to approve
     * @throws Exception If the request is already approved or if the team leader did
     * not approve the request
     */
    void approveByLicenseManager (Integer requestId) throws Exception;
    
    /**
     * This method creates a new purchase request
     * @param program The program to which you want to create the purchase request
     * @param appUser The user that wants to create the purchase request
     * @return An id number of the newly created purchase request
     */
    Integer createNewPurchaseRequest(Program program, AppUser appUser);
    
    /**
     * This method sets a purchase request as approved by the manager
     * @param requestId The request id of the request that you want to approve
     * @throws Exception If the purchase request is already approved by the manager
     */
    void approveByManager(Integer requestId) throws Exception;
    
    /**
     * Writes new log into license history table
     * @param licenseHistory The license history that we want to add
     */
    void logLicenseHistory(LicenseHistory licenseHistory);
 
    /**
     * This method tries to find a free license and to assign it to the user
     * that made the license request
     * @param requestId The user that created the request id will receive the license
     * @throws Exception If there are no free licenses or if the request was not approved
     * by the license manager
     */
    void assignFreeLicense(Integer requestId) throws Exception;

    /**
     * This method is used to return a license that belongs to some user (e.g.
     * make it free again)
     * @param licenseId The license id that you want to return
     * @throws Exception If this license does not belong to any user
     */
    void returnLicense(String licenseId) throws Exception;

    /**
     * Marks this purchase request as completed
     * @param purchaseFlow The purchase flow that you want to mark as completed
     */
    void purchaseRequestCompleted(PurchaseFlow purchaseFlow);

}