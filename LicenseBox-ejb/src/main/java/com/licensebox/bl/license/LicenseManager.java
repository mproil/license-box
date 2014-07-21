package com.licensebox.bl.license;

import com.licensebox.bl.email.LicenseBoxEmailLocal;
import com.licensebox.bl.exception.LicenseBoxUncheckedException;
import com.licensebox.db.dao.AppUserDaoLocal;
import com.licensebox.db.dao.LicenseDaoLocal;
import com.licensebox.db.dao.LicenseFlowDaoLocal;
import com.licensebox.db.dao.LicenseHistoryDaoLocal;
import com.licensebox.db.dao.ProgramDaoLocal;
import com.licensebox.db.dao.PurchaseFlowDaoLocal;
import com.licensebox.db.entity.AppRole;
import com.licensebox.db.entity.AppUser;
import com.licensebox.db.entity.License;
import com.licensebox.db.entity.LicenseFlow;
import com.licensebox.db.entity.LicenseHistory;
import com.licensebox.db.entity.LicenseHistoryPK;
import com.licensebox.db.entity.Program;
import com.licensebox.db.entity.PurchaseFlow;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author Anna Guzman & Michael Paltsev
 */
@Stateless
@LocalBean
public class LicenseManager implements LicenseManagerLocal{

    //<editor-fold desc="EJB Injection" defaultstate="collapsed">
    @EJB
    LicenseDaoLocal        licenseDao;
    
    @EJB
    LicenseHistoryDaoLocal licenseHistoryDao; 
    
    @EJB
    LicenseFlowDaoLocal    licenseFlowDao;
    
    @EJB
    PurchaseFlowDaoLocal   purchaseFlowDao;
    
    @EJB 
    LicenseBoxEmailLocal   licenseBoxEmailDao;
    
    @EJB
    AppUserDaoLocal        appUserDao;
    
    @EJB
    ProgramDaoLocal        programDao;
    //</editor-fold>
    
    @PostConstruct
    public void init() {
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer createNewLicenseRequest(String username, Integer programId){
        
        LicenseFlow newRequest; 
        Integer requestId;
        String teamLeaderUsername;
        String fullName;
        String email;
        
        Program program = this.programDao.getByProgramId(programId);
        AppUser appUser = this.appUserDao.getUserByUsername(username);
        
        newRequest = new LicenseFlow(appUser, program);
        requestId = this.licenseFlowDao.createRequest(newRequest);
        teamLeaderUsername = this.licenseFlowDao.getTeamleadForRequest(requestId);
        if (!teamLeaderUsername.isEmpty()) {
            fullName = this.appUserDao.getUserFullName(teamLeaderUsername);
            email = this.appUserDao.getUserByUsername(teamLeaderUsername).getEmail();

            //Send e-mail nofification to respective team leader
            String subject = "Message From LicenseBox";
            String message = String.format("Dear %s,\n\nThere is a new license request waiting for you approval."
                    + "\n\n\nSincerely,\nThe LicenseBox Team", 
                    fullName);
            this.licenseBoxEmailDao.sendMessage(fullName, subject, message, email);
        }
        return requestId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void approveByTeamLeader (Integer requestId, String teamLeader) throws Exception {
        
        LicenseFlow toApprove;
        AppUser licManUser;
        String fullName;
        String email;
                
        toApprove = licenseFlowDao.getByRequestId(requestId);
        
        if (!this.licenseFlowDao.getTeamleadForRequest(requestId).equals(teamLeader)){  
            String message = "You are not allowed to approve this request";
            throw new Exception(message);
        }
        
        if ((!toApprove.getTeamleadApproved()) && (!toApprove.getLicmanApproved())){
            toApprove.setTeamleadApproved(true);        
            this.licenseFlowDao.editRequest(toApprove);
        }
        else {
            String message = "The request is already approved";
            throw new Exception(message);
        }
        
        //send e-mail notofication to license manager
        licManUser = this.appUserDao.getUserByRolename(AppRole.RoleName.LICENSE_MANAGER).get(0);
        fullName = this.appUserDao.getUserFullName(licManUser.getUsername());
        email = licManUser.getEmail();
        
        //Send e-mail nofification to license Manager
        String subject = "Message From LicenseBox";
        String message = String.format("Dear %s,\n\nThere is a new license request waiting for you approval."
                + "\n\n\nSincerely,\nThe LicenseBox Team", 
                fullName);
        this.licenseBoxEmailDao.sendMessage(fullName, subject, message, email);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void approveByLicenseManager(Integer requestId) throws Exception {
        
        LicenseFlow toApprove;
        toApprove = licenseFlowDao.getByRequestId(requestId);
        
        if ((toApprove.getTeamleadApproved()) && (!toApprove.getLicmanApproved())) {
            try {
                toApprove.setLicmanApproved(true);        
                licenseFlowDao.editRequest(toApprove);
                assignFreeLicense(toApprove.getRequestId());
            } catch (Exception ex) {
                throw new LicenseBoxUncheckedException(ex.getMessage());
            }
        }
        else {
            String message = "The request is already approved by license manager"
                    + " or still not approved by team leader";
            throw new Exception(message);
        }
        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void logLicenseHistory(LicenseHistory licenseHistory) {
        licenseHistoryDao.createHistoryEntry(licenseHistory);    
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Integer createNewPurchaseRequest(Program program, AppUser appUser) {
        
        Integer purchaseId;
        AppUser managerUser;
        String fullName;
        String email;
        
        PurchaseFlow newPurchase = new PurchaseFlow();
        newPurchase.setProgram(program);
        newPurchase.setAppUser(appUser);
        newPurchase.setManagerApproved(Boolean.FALSE);
        newPurchase.setPurchaseClosed(Boolean.FALSE);
        newPurchase.setPurchaseDate(null);
        
        purchaseId = purchaseFlowDao.createPurchaseFlow(newPurchase);                      
        
        //send e-mail notification to manager
        List<AppUser> managerList = this.appUserDao.getUserByRolename(AppRole.RoleName.MANAGER);
        if ((managerList != null) && (!managerList.isEmpty())) {
            managerUser = managerList.get(0); //there is only one result
            fullName = this.appUserDao.getUserFullName(managerUser.getUsername());
            email = managerUser.getEmail();

            String subject = "Message From LicenseBox";
            String message = String.format("Dear %s,\n\nThere is a new purchase request waiting for you approval."
                    + "\n\n\nSincerely,\nThe LicenseBox Team", 
                    fullName);
            this.licenseBoxEmailDao.sendMessage(fullName, subject, message, email);
        }
        return purchaseId;
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void approveByManager(Integer requestId) throws Exception {
        
        PurchaseFlow toApprove;
        toApprove = purchaseFlowDao.getPurchaseFlow(requestId);
              
        if (!toApprove.getManagerApproved()){
            toApprove.setManagerApproved(true);        
            purchaseFlowDao.editPurchaseFlow(toApprove);
        }
        else {
            String message = "The purchase request is already approved by manager";                  
            throw new Exception(message);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void assignFreeLicense(Integer requestId) throws Exception {
        
        LicenseFlow request = this.licenseFlowDao.getByRequestId(requestId);
        
        if (request.getLicmanApproved()) {
            List<License> licenseList = this.licenseDao.getFreeLicensesByProgram(request.getProgram());
            
            if ((licenseList == null) || (licenseList.isEmpty())) {
                String programName = request.getProgram().getProgramName();
                String programVersion = request.getProgram().getVersion();
                throw new LicenseBoxUncheckedException("Could not find a free license for program " + programName + " " + programVersion);
            }
            
            License license = licenseList.get(0);
            license.setAppUser(request.getAppUser());
            this.licenseDao.updateLicense(license);
            
            LicenseHistory licenseHistory = new LicenseHistory();
            LicenseHistoryPK licenseHistoryPK = new LicenseHistoryPK();
            licenseHistoryPK.setLicenseId(license.getLicenseId());
            licenseHistoryPK.setUsername(license.getAppUser().getUsername());
            licenseHistoryPK.setStartDate(new Date());
            licenseHistory.setLicenseHistoryPK(licenseHistoryPK);
            licenseHistory.setAppUser(license.getAppUser());
            
            logLicenseHistory(licenseHistory);
        } else {
            throw new Exception("The license request with the Id " + requestId + " was not approved by the license manager");
        }
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void returnLicense(String licenseId) throws Exception {
        if (licenseId != null) {
            License license = this.licenseDao.getLicenseById(licenseId);
            if (license.getAppUser() != null) {
                LicenseHistory licenseHistory = this.licenseHistoryDao.getLastFreeEntryByLicenseId(licenseId);
                if (licenseHistory != null) {
                    licenseHistory.setEndDate(new Date());
                    this.licenseHistoryDao.editHistoryEntry(licenseHistory);
                }
                license.setAppUser(null);
                this.licenseDao.updateLicense(license);
            } else {
                throw new Exception("The license " + licenseId + " does not belong to any user.");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void purchaseRequestCompleted(PurchaseFlow purchaseFlow) {
        purchaseFlow.setPurchaseClosed(true);
        this.purchaseFlowDao.editPurchaseFlow(purchaseFlow);
    }
    
}