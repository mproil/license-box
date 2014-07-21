package com.licensebox.ui.page.manager;

import com.licensebox.bl.license.LicenseManagerLocal;
import com.licensebox.db.dao.PurchaseFlowDaoLocal;
import com.licensebox.db.entity.PurchaseFlow;
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
 * This Managed Bean is the controller of the view manager/purchaseRequests.xhtml
 * 
 * @author Michael Paltsev & Anna Guzman
 */
@ManagedBean
@ViewScoped
public class PurchaseApprovalManagedBean implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="EJB Injection">
    @EJB
    private LicenseManagerLocal licenseManager;
    
    @EJB
    private PurchaseFlowDaoLocal purchaseFlowDao;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private List<PurchaseFlow> purchaseFlows;
    private PurchaseFlow selectedPurchaseFlow;
    
    private String username;
    private Logger logger = Logger.getLogger(PurchaseApprovalManagedBean.class);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters & Setters">
    public PurchaseFlow getSelectedPurchaseFlow() {
        return selectedPurchaseFlow;
    }
    
    public void setSelectedPurchaseFlow(PurchaseFlow selectedPurchaseFlow) {
        this.selectedPurchaseFlow = selectedPurchaseFlow;
    }
    
    public List<PurchaseFlow> getPurchaseFlows() {
        return purchaseFlows;
    }
    //</editor-fold>
    
    @PostConstruct
    public void init() {
        this.purchaseFlows = this.purchaseFlowDao.getForManagerApproval();
        this.username = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
    }
  
    /**
     * This method is called when the manager wants to approve a selected purchase
     */
    public void approvePurchase() {
        if (this.selectedPurchaseFlow != null) {
            try {
                this.licenseManager.approveByManager(this.selectedPurchaseFlow.getPurchaseId());
                this.purchaseFlows = this.purchaseFlowDao.getForManagerApproval();
                logger.info("Manager " + this.username + " approved the purchase id "
                        + this.selectedPurchaseFlow.getPurchaseId());
                Helper.addFacesMessage(Helper.INFO, "Purchase was approved successfully");
            } catch (Exception ex) {
                Helper.addFacesMessage(Helper.ERROR, ex.getMessage());                

            }
            this.selectedPurchaseFlow = null;
        } else {
            Helper.addFacesMessage(Helper.ERROR, "Couldn't get the selected purchase request.\n"
                    + "Please refresh the page and try again.");
        }
    }
}
