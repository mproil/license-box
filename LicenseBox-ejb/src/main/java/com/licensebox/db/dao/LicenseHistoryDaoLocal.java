package com.licensebox.db.dao;

import com.licensebox.db.entity.License;
import com.licensebox.db.entity.LicenseHistory;
import java.util.List;
import javax.ejb.Local;

/**
 * This interface defines the methods that are used in the LicenseHistory DAO
 * 
 * @author Anna Guzman & Michael Paltsev
 */
@Local
public interface LicenseHistoryDaoLocal {

    /**
     * Creates a new LicenseHistory entry in the database
     * @param licenseHistory The LicenseHistory entry that we want to add to the
     * database
     */
    void createHistoryEntry(LicenseHistory licenseHistory);

    /**
     * Changes a LicenseHistory entry in the database
     * @param licenseHistory The LicenseHistory entry that we want to change
     */
    void editHistoryEntry(LicenseHistory licenseHistory);

    /**
     * Returns a List of history entries for specific license
     * @param license The license for which we want to search
     * @return A List of LicenseHistory entries
     */
    List<LicenseHistory> getLicenseHistory(License license);
    
    /**
     * Returns all license history for user
     * @param username The username for which we want to get the license history
     * @return A List of all LicenseHistory entries or a null object if none found
     */    
    List<LicenseHistory> getLicenceHistoryByUsername(String username);

    /**
     * Returns all license history stored in the database
     * @return A List of all LicenseHistory entries or a null object if none found
     */
    List<LicenseHistory> getAll();         

    /**
     * Finds the history entry that has no end date that belongs to a
     * specific license id
     * @param licenseId The license id whose history entry you want to find
     * @return A HistoryEntry or null if none found
     */
    LicenseHistory getLastFreeEntryByLicenseId(String licenseId);

    /**
     * Returns all the license history of a specific license
     * @param licenseId The license id whose license history you want to find
     * @return A List of LicenseHistory objects or null if none found
     */
    List<LicenseHistory> getLicenseHistoryByLicenseId(String licenseId);
    
}