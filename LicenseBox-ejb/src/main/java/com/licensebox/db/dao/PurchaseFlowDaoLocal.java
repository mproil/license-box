package com.licensebox.db.dao;

import com.licensebox.db.entity.PurchaseFlow;
import java.util.List;
import javax.ejb.Local;

/**
 * This Interface defines the methods that are used by the PurchaseFlow DAO
 * 
 * @author Anna Guzman & Michael Paltsev
 */
@Local
public interface PurchaseFlowDaoLocal {

    /**
     * This method adds a new purchase request to the database
     * @param purchaseFlow The purchase request that you want to add
     * @return The purchase request integer id
     */
    Integer createPurchaseFlow(PurchaseFlow purchaseFlow);

    /**
     * This method updates the purchase request in the database
     * @param purchaseFlow The purchase request that you want to update
     */
    void editPurchaseFlow(PurchaseFlow purchaseFlow);

    /**
     * This method returns a purchase flow that matches the purchase flow id
     * @param requestId The purchase flows id that you want to search
     * @return A PurchaseFlow or a null object if none found
     */
    PurchaseFlow getPurchaseFlow(Integer requestId);

    /**
     * Finds all the purchase request from the database
     * @return A List of PurchaseFlow objects or a null object if none found
     */
    List<PurchaseFlow> getAll();

    /**
     * Finds all the purchase requests that are awaiting for the Manager approval
     * @return A List of PurchaseFlow objects or a null object if none found
     */
    List<PurchaseFlow> getForManagerApproval();
    
}