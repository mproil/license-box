package com.licensebox.ui.page.licenseadmin;

import com.licensebox.db.dao.ProgramDaoLocal;
import com.licensebox.db.entity.Program;
import com.licensebox.ui.Helper;
import java.io.Serializable;
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

/**
 * This Managed Bean is the controller of the view programManage.xhtml
 * 
 * @author Michael Paltsev & Anna Guzman
 */
@ManagedBean
@ViewScoped
public class ProgramsManagedBean implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="EJB Injection">
    @EJB
    private ProgramDaoLocal programDao;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private List<Program> programs;
    private Program selectedProgram;
    private Integer programId;
    private String programName;
    private String version;
    private String siteLink;
    
    private String username;
    private Logger logger = Logger.getLogger(ProgramsManagedBean.class);
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Getters & Setters">
    public List<Program> getPrograms() {
        return this.programs;
    }
    
    public void setSelectedProgram(Program selectedProgram) {
        this.selectedProgram = selectedProgram;
    }
    
    public Program getSelectedProgram() {
        return this.selectedProgram;
    }
    
    public Integer getProgramId() {
        return programId;
    }
    
    public void setProgramId(Integer programId) {
        this.programId = programId;
    }
    
    public String getProgramName() {
        return programName;
    }
    
    public void setProgramName(String programName) {
        this.programName = programName;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getSiteLink() {
        return siteLink;
    }
    
    public void setSiteLink(String siteLink) {
        this.siteLink = siteLink;
    }
    //</editor-fold>
    
    @PostConstruct
    public void init() {
        this.programs = this.programDao.getAll();
        this.username = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
    }
    
    /**
     * This method saves the selected program to the database
     */
    public void saveSelectedProgram() {
        if (this.selectedProgram != null) {
            
            // Add the http:// prefix if not found
            String tempSiteLink = this.selectedProgram.getSiteLink();
            if ((tempSiteLink != null) && (!tempSiteLink.isEmpty()) && (!tempSiteLink.startsWith("http://"))) {
                tempSiteLink = "http://" + tempSiteLink;
                this.selectedProgram.setSiteLink(tempSiteLink);
            }
            this.programDao.editProgram(this.selectedProgram);
            logger.info("The license admin " + this.username + " edited the program " + 
                    this.selectedProgram.getProgramName() + " " + this.selectedProgram.getVersion());
            Helper.addFacesMessage(Helper.INFO, "Saved selected program successfully");
            this.selectedProgram = null;
        }
    }
    
    public void createNewPorgram() {
        Program newProgram = new Program();
        newProgram.setProgramId(this.programId);
        newProgram.setProgramName(this.programName);
        newProgram.setVersion(this.version);
        if ((this.siteLink != null) && (!this.siteLink.isEmpty()) && (!this.siteLink.startsWith("http://"))) {
            this.siteLink = "http://" + this.siteLink;
        }
        newProgram.setSiteLink(this.siteLink);
        try {
            this.programDao.createProgram(newProgram);
            this.programs = this.programDao.getAll();
            logger.info("The license admin " + this.username + " added the program " + this.programName + " " +
                    this.version);
            Helper.addFacesMessage(Helper.INFO, "Created a new program successfully");
            this.programId = null;
            this.programName = null;
            this.version = null;
            this.siteLink = null;
        } catch (Exception ex) {
            Helper.addFacesMessage(Helper.ERROR, ex.getMessage());

        }
    }
    
    /**
     * This method is used to validate the program id
     * @param context
     * @param component
     * @param value
     * @throws ValidatorException 
     */
    public void validateProgramId(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value instanceof Integer) {
            if (isExistsProgramId((Integer)value)) {
                throw new ValidatorException(
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "This program id already exists"));
            }
        } else {
            throw new ValidatorException(
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "This is not a valid program id"));
        }
    }
    
    
    private boolean isExistsProgramId(Integer programId) {
        return (this.programDao.getByProgramId(programId) != null) ? true : false;
    }
    
}