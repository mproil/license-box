package com.licensebox.ui.page.licenseadmin;

import com.licensebox.bl.license.LicenseManagerLocal;
import com.licensebox.db.dao.LicenseDaoLocal;
import com.licensebox.db.dao.LicenseHistoryDaoLocal;
import com.licensebox.db.dao.ProgramDaoLocal;
import com.licensebox.db.entity.License;
import com.licensebox.db.entity.LicenseHistory;
import com.licensebox.db.entity.Program;
import com.licensebox.ui.Helper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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
import javax.servlet.http.Part;
import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 * This Managed Bean is the controller of the licenseManage.xhtml
 * view.
 * 
 * @author Michael Paltsev & Anna Guzman
 */
@ManagedBean
@ViewScoped
public class LicenseManagedBean implements Serializable {

    //<editor-fold desc="EJB Injection" defaultstate="collapsed">
    @EJB
    private LicenseDaoLocal licenseDao;
    
    @EJB
    private ProgramDaoLocal programDao;
    
    @EJB
    private LicenseManagerLocal licenseManager;
    
    @EJB
    private LicenseHistoryDaoLocal licenseHistoryDao;
    //</editor-fold>
    
    //<editor-fold desc="Properties" defaultstate="collapsed">
    private List<License> licenses;
    private List<Program> programs;
    private License selectedLicense;
    
    private String licenseId;
    private Program program;
    private BigDecimal price;
    private Date purchaseDate;
    private Part file;
    private byte[] attachment;
    
    private final int BUFFER_SIZE = 1024;
    public static final String CONTENT_TYPE = "application/pdf";
    
    private String username;
    private Logger logger = Logger.getLogger(LicenseManagedBean.class);
    //</editor-fold>
    
    //<editor-fold desc="Getters & Setters" defaultstate="collapsed">
    public List<License> getLicenses() {
        return this.licenses;
    }
    
    public License getSelectedLicense() {
        return this.selectedLicense;
    }
    
    public void setSelectedLicense(License selectedLicense) {
        this.selectedLicense = selectedLicense;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getLicenseId() {
        return licenseId;
    }

    public Program getProgram() {
        return program;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public List<Program> getPrograms() {
        return programs;
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }
    //</editor-fold>
    
    @PostConstruct
    public void init() {
        this.licenses = this.licenseDao.getAll();
        this.programs = this.programDao.getAll();
        this.username = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
    }
    
    /**
     * This method saves the changes to the selected license
     */
    public void saveSelectedLicense() {
        if (this.selectedLicense != null) {
            try {
                this.licenseDao.updateLicense(this.selectedLicense);
                logger.info("The license admin " + this.username + " made changes to the license " + this.selectedLicense.getLicenseId());
                Helper.addFacesMessage(Helper.INFO, "License was saved successfully");
                this.selectedLicense = null;
            } catch (Exception ex) {
                Helper.addFacesMessage(Helper.ERROR, ex.getMessage());
            }
        } else {
            Helper.addFacesMessage(Helper.ERROR, "Couldn't get the selected license.\nPlease refresh the page and try again.");
        }
    }
    
    /**
     * This method creates a new license
     */
    public void createNewLicense() {
        try {
            License license = new License(this.licenseId, this.price, this.purchaseDate, this.program);
            this.licenseDao.createNewLicene(license);
            this.licenses = this.licenseDao.getAll();
            logger.info("The license admin " + this.username + " created a new license: " + this.licenseId);
            Helper.addFacesMessage(Helper.INFO, "A new license was created successfully");
            this.licenseId = null;
            this.price = null;
            this.purchaseDate = null;
            this.program = null;
        } catch (Exception ex) {
             Helper.addFacesMessage(Helper.ERROR, ex.getMessage());
        }
    }
    
    private boolean isLicenseIdExists(String licenseId) {
        return !(this.licenseDao.getLicenseById(licenseId) == null);
    }
    
    /**
     * This method is used to validate the license id that is entered in the create
     * new license dialog on the JSF page
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
    
    /**
     * This method allows the License Administrator to return a selected license e.g.
     * to get the license from a user and make it free again
     */
    public void returnSelectedLicense() {
        if (this.selectedLicense != null) {
            try {
                String tempUsername = this.selectedLicense.getAppUser().getUsername();
                this.licenseManager.returnLicense(selectedLicense.getLicenseId());
                this.licenses = this.licenseDao.getAll();
                logger.info("The license admin " + this.username + " returned a license that belonged to " + tempUsername);
                Helper.addFacesMessage(Helper.INFO, "You've returned the license that belonged to " + tempUsername);
                this.selectedLicense = null;
            } catch (Exception ex) {
                Helper.addFacesMessage(Helper.ERROR, ex.getMessage());
            }
        } else {
            Helper.addFacesMessage(Helper.ERROR, "Couldn't get the selected license.\nPlease refresh the page and try again.");
        }
    }
    
    /**
     * This method finds and returns the selected license history
     * @return A List of LicenseHistory objects for the selected license
     */
    public List<LicenseHistory> getSelectedLicenseHistory() {
        if (this.selectedLicense != null) {
            return this.licenseHistoryDao.getLicenseHistoryByLicenseId(this.selectedLicense.getLicenseId());
        } else {
            return null;
        }
    }
 
    /**
     * This method handles the file upload
     */
    public void handleFileUpload() {
        try {
            if ((this.selectedLicense == null) || (this.file == null)){
                throw new Exception("Please refresh the page and try again");
            }
            
            if (!this.file.getContentType().equals(CONTENT_TYPE)) {
                throw new Exception("Please upload only PDF files");
            }
            
            try (InputStream inputStream = this.file.getInputStream()) {
                try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            
                    int read = 0;
                    byte[] bytes = new byte[BUFFER_SIZE];

                    while ((inputStream != null) && (read = inputStream.read(bytes)) != -1) {
                        out.write(bytes, 0, read);
                    }
                    
                    this.attachment = out.toByteArray();
                    this.selectedLicense.setAttachement(this.attachment);
                    this.licenseDao.updateLicense(this.selectedLicense);
                    logger.info(this.username + " uploaded the file " + file.getSubmittedFileName() + " as an "
                            + "attachment to the license " + this.selectedLicense.getLicenseId());
                    Helper.addFacesMessage(Helper.INFO, "File uploaded successfully");
                    this.attachment = null;
                    this.selectedLicense = null;
                }
            }
        } catch (Exception ex) {
            Helper.addFacesMessage(Helper.ERROR, ex.getMessage());
        }
    }
       
}