package com.licensebox.ui.page.licenseadmin;

import com.licensebox.bl.license.LicenseManagerLocal;
import com.licensebox.db.dao.LicenseFlowDaoLocal;
import com.licensebox.db.entity.LicenseFlow;
import com.licensebox.ui.Helper;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;

/**
 * This Managed Bean is the controller of the app/licman/requests.xhtml
 * view
 * 
 * @author Michael Paltsev & Anna Guzman
 */
@ManagedBean
@ViewScoped
public class LicenseRequestsManagedBean implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="EJB Injection">
    @EJB
    private LicenseManagerLocal licenseManager;
    
    @EJB
    private LicenseFlowDaoLocal licenseFlowDao;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private List<LicenseFlow> licenseRequests;
    private LicenseFlow selectedLicenseFlow;
    
    private String username;
    private Logger logger = Logger.getLogger(LicenseRequestsManagedBean.class);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters & Setters">
    public List<LicenseFlow> getLicenseRequests() {
        return licenseRequests;
    }
    
    public void setSelectedLicenseFlow(LicenseFlow selectedLicenseFlow) {
        this.selectedLicenseFlow = selectedLicenseFlow;
    }
    
    public LicenseFlow getSelectedLicenseFlow() {
        return selectedLicenseFlow;
    }
    //</editor-fold>
    
    @PostConstruct
    public void init() {
        this.licenseRequests = this.licenseFlowDao.getForLicmanApproval();
        this.username = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
    }
    
    /**
     * Allows the license manager to approve the license request
     * 
     */
    public void approveLicenseRequest() {
        if (this.selectedLicenseFlow != null) {
            try {
                this.licenseManager.approveByLicenseManager(selectedLicenseFlow.getRequestId());
                this.licenseRequests = this.licenseFlowDao.getForLicmanApproval();
                logger.info("The license admin " + this.username + " approved the license request "
                        + this.selectedLicenseFlow.getRequestId());
                Helper.addFacesMessage(Helper.INFO, "Request approved successfully");
                this.selectedLicenseFlow = null;
            } catch (Exception ex) {
               Helper.addFacesMessage(Helper.ERROR, ex.getMessage());
            }
        } else {
            Helper.addFacesMessage(Helper.ERROR, "Couldn't get the selected license request.\nPlease refresh the page and try again.");
        }
    }
    
    /**
     * This method is used to create a purchase request for a new license
     */
    public void askToPurchase() {
        if (this.selectedLicenseFlow != null) {
            try {
                this.licenseManager.createNewPurchaseRequest(this.selectedLicenseFlow.getProgram(),
                        this.selectedLicenseFlow.getAppUser());
                logger.info("The license admin " + this.username + " asked to purchase a new license for "
                        + "the program " + this.selectedLicenseFlow.getProgram().getProgramName() + " "
                        + this.selectedLicenseFlow.getProgram().getVersion());
                Helper.addFacesMessage(Helper.INFO, "A purchase request was created successfully");
                this.selectedLicenseFlow = null;
            } catch (Exception ex) {
                Helper.addFacesMessage(Helper.ERROR, ex.getMessage());
            }
        } else {
            Helper.addFacesMessage(Helper.ERROR, "Couldn't get the selected license request.\nPlease refresh the page and try again.");
        }
    }
    
}