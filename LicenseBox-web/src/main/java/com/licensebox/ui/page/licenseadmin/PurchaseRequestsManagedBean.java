package com.licensebox.ui.page.licenseadmin;

import com.licensebox.bl.license.LicenseManagerLocal;
import com.licensebox.db.dao.LicenseDaoLocal;
import com.licensebox.db.dao.PurchaseFlowDaoLocal;
import com.licensebox.db.entity.License;
import com.licensebox.db.entity.PurchaseFlow;
import com.licensebox.ui.Helper;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import org.apache.log4j.Logger;
import org.primefaces.model.UploadedFile;

/**
 * This Managed Bean acts as the controller of the view licman/purchaseRequests.xhtml
 * 
 * @author Michael Paltsev & Anna Guzman
 */
@ManagedBean
@ViewScoped
public class PurchaseRequestsManagedBean implements Serializable {

    
    //<editor-fold defaultstate="collapsed" desc="EJB Injection">
    @EJB
    private LicenseManagerLocal licenseManager;
    
    @EJB
    private PurchaseFlowDaoLocal purchaseFlowDao;
    
    @EJB
    private LicenseDaoLocal licenseDao;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Properties">
    private List<PurchaseFlow> purchaseRequests;
    private PurchaseFlow selectedPurchaseFlow;
    
    private String licenseId;
    private BigDecimal price;
    private Date purchaseDate;
    private UploadedFile file;
    
    private String username;
    private Logger logger = Logger.getLogger(PurchaseRequestsManagedBean.class);
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Getters & Setters">
    public List<PurchaseFlow> getPurchaseRequests() {
        return purchaseRequests;
    }
    
    public void setSelectedPurchaseFlow(PurchaseFlow selectedPurchaseFlow) {
        this.selectedPurchaseFlow = selectedPurchaseFlow;
    }
    
    public PurchaseFlow getSelectedPurchaseFlow() {
        return selectedPurchaseFlow;
    }
    
    public String getLicenseId() {
        return this.licenseId;
    }
    
    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }
    
    public BigDecimal getPrice() {
        return this.price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public Date getPurchaseDate() {
        return purchaseDate;
    }
    
    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
    
    public UploadedFile getFile() {
        return this.file;
    }
    
    public void setFile(UploadedFile file) {
        this.file = file;
    }
    //</editor-fold>
    
    
    @PostConstruct
    public void init() {
        this.purchaseRequests = this.purchaseFlowDao.getAll();
        this.username = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
    }
    
    /**
     * Adds a new license to the database
     */
    public void purchaseLicense() {
        if (this.selectedPurchaseFlow != null) {
            try {
                License license = new License(this.licenseId, this.price, this.purchaseDate, this.selectedPurchaseFlow.getProgram());
                this.licenseDao.createNewLicene(license);
                this.licenseManager.purchaseRequestCompleted(selectedPurchaseFlow);
                logger.info("The license admin " + this.username + " has purchased the license " + this.licenseId);
                Helper.addFacesMessage(Helper.INFO, "A new license was purchased");
                this.selectedPurchaseFlow = null;
                this.licenseId = null;
                this.price = null;
                this.purchaseDate = null;
            } catch (Exception ex) {
                Helper.addFacesMessage(Helper.ERROR, ex.getMessage());

            }
        } else {
            Helper.addFacesMessage(Helper.ERROR, "Couldn't get the selected license purchase request.\n"
                    + "Please refresh the page and try again.");

        } 
    }
    
    private boolean isLicenseIdExists(String licenseId) {
        return !(this.licenseDao.getLicenseById(licenseId) == null);
    }
    
    /**
     * This method is used to validate the license id
     * @param context
     * @param component
     * @param value
     * @throws ValidatorException 
     */
    public void licenseIdValidator(FacesContext context, UIComponent component, Object value)
            throws ValidatorException {
        String tempLicenseId = (String)value;
        if ((tempLicenseId == null) || (tempLicenseId.isEmpty())) {
            throw new ValidatorException(
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Please enter a license id"));
        }
        
        if (isLicenseIdExists(tempLicenseId)) {
            throw new ValidatorException(
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "This license id already exists"));
        }
    }

}