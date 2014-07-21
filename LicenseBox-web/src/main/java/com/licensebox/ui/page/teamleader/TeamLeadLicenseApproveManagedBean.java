package com.licensebox.ui.page.teamleader;

import com.licensebox.bl.license.LicenseManagerLocal;
import com.licensebox.db.dao.LicenseFlowDaoLocal;
import com.licensebox.db.entity.LicenseFlow;
import com.licensebox.ui.Helper;
import com.licensebox.ui.session.UserSessionManagedBean;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;


/**
 * This Managed Bean acts as a controller for the view teamman/requests.xhtml
 *
 * @author Michael Paltsev & Anna Guzman
 */
@ManagedBean
@ViewScoped
public class TeamLeadLicenseApproveManagedBean implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="EJB Injection">
    @EJB
    private LicenseManagerLocal licenseManager;
    
    @EJB
    private LicenseFlowDaoLocal licenseFlowDao;
    
    @ManagedProperty("#{userSessionManagedBean}")
    private UserSessionManagedBean userSessionManagedBean;
    //</editor-fold>
   
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private List<LicenseFlow> licenseRequests;
    private LicenseFlow licenseFlow;
    
    private Logger logger = Logger.getLogger(TeamLeadLicenseApproveManagedBean.class);
    private String teamLeadUsername;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters & Setters">
    public List<LicenseFlow> getLicenseRequests() {
        return licenseRequests;
    }
    
    public void setLicenseFlow(LicenseFlow licenseFlow) {
        this.licenseFlow = licenseFlow;
    }
    
    public LicenseFlow getLicenseFlow() {
        return licenseFlow;
    }
    
    public void setUserSessionManagedBean(UserSessionManagedBean userSessionManagedBean) {
        this.userSessionManagedBean = userSessionManagedBean;
    }
    //</editor-fold>
    
    @PostConstruct
    public void init() {
        this.teamLeadUsername = this.userSessionManagedBean.getUsername();
        this.licenseRequests = this.licenseFlowDao.getForTeamleadApproval(this.teamLeadUsername);
    }
    
    public void approveLicenseRequest(LicenseFlow licenseFlow) {
        try {
            this.licenseManager.approveByTeamLeader(licenseFlow.getRequestId(), this.teamLeadUsername);
            this.licenseRequests = this.licenseFlowDao.getForTeamleadApproval(this.teamLeadUsername);
            logger.info("Team leader " + this.teamLeadUsername + " approved the license reqeust " + licenseFlow.getRequestId());
            Helper.addFacesMessage(Helper.INFO, "License request approved successfully");
        } catch (Exception ex) {
            Helper.addFacesMessage(Helper.ERROR, ex.getMessage());
        }
    }
}
