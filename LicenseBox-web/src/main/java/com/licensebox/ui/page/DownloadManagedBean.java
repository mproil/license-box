package com.licensebox.ui.page;

import com.licensebox.db.entity.License;
import com.licensebox.ui.Helper;
import com.licensebox.ui.page.licenseadmin.LicenseManagedBean;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 * This Managed Bean is responsible for downloading attachments
 * 
 * @author Michael Paltsev & Anna Guzman
 */
@ManagedBean
@ViewScoped
public class DownloadManagedBean implements Serializable {
    
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private StreamedContent file;
    private License selectedLicense;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Getters & Setters">
    public void setSelectedLicense(License selectedLicense) {
        this.selectedLicense = selectedLicense;
    }
    
    public License getSelectedLicense() {
        return this.selectedLicense;
    }
    //</editor-fold>
    
    /**
     * This method "downloads" the file from the database according to the
     * license that is selected. If no license is selected, null is returned.
     * 
     * @return A StreamedContent object representing the file that is downloaded
     */
    public StreamedContent getFile() {
        if (this.selectedLicense != null) {

            String tempFileName = String.format("%s_%s_%s.pdf", new Object[] {this.selectedLicense.getProgram().getProgramName(),
                                                                                    this.selectedLicense.getLicenseId(),
                                                                                    "attach"});
            InputStream in = new ByteArrayInputStream(this.selectedLicense.getAttachement());
            this.file = new DefaultStreamedContent(in, LicenseManagedBean.CONTENT_TYPE, tempFileName);
        } else {
            Helper.addFacesMessage(Helper.ERROR, "Could not get the selected license. Please refresh the page.");
        }
        return this.file;
    }
    
}
