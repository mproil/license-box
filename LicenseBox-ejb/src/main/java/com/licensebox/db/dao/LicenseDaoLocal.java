package com.licensebox.db.dao;

import com.licensebox.db.entity.AppUser;
import com.licensebox.db.entity.License;
import com.licensebox.db.entity.Program;
import java.util.List;
import javax.ejb.Local;

/**
 * An Interface for the License DAO that defines all the methods that will
 * be in use.
 * 
 * @author Anna Guzman & Michael Paltsev
 */
@Local
public interface LicenseDaoLocal {

    /**
     * This method inserts a new license to the database
     * @param license The license that you want to insert
     */
    void createNewLicene(License license);

    /**
     * This method updates a license in the database
     * @param license The license that you want to update
     */
    void updateLicense(License license);

    /**
     * This method returns a license that has the specific id
     * @param licenseId The license id you wish to search for
     * @return A License with this id or a null object
     */
    License getLicenseById(String licenseId);
    
    /**
     * This method returns all the licenses that belong to the specific Program
     * that has this program Id
     *
     * @param program The program whose licenses we want to find
     * @return A List of licenses that belong to the program id or a null object
     * if no licenses are found
     */
    
    List<License> getLicensesByProgram(Program program);
    
    /**
     * This method returns all the free licenses (e.g. not assigned to any 
     * user) that belong to this program
     * @param program The program whose free licenses we want to find
     * @return A List of licenses or a null object if none are found
     */
    List<License> getFreeLicensesByProgram(Program program);

    /**
     * This method returns all the licenses that appear in the database
     * @return A List of all the licenses or a null object if none are found
     */
    List<License> getAll();

    /**
     * Returns all the licenses that belong to the specific AppUser
     * @param appUser The AppUser whose licenses you want to get
     * @return A List of licenses or a null object if no licenses are found
     */
    List<License> getLicensesByAppUser(AppUser appUser);

}
