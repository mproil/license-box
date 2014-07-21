package com.licensebox.db.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This entity represents the purchase_flow table in the database
 * 
 * @author Anna Guzman & Michael Paltsev
 */
@Entity
@Table(name = "purchase_flow")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PurchaseFlow.findAll", query = "SELECT p FROM PurchaseFlow p"),
    @NamedQuery(name = "PurchaseFlow.findByPurchaseId", query = "SELECT p FROM PurchaseFlow p WHERE p.purchaseId = :purchaseId"),
    @NamedQuery(name = "PurchaseFlow.findByManagerApproved", query = "SELECT p FROM PurchaseFlow p WHERE p.managerApproved = :managerApproved"),
    @NamedQuery(name = "PurchaseFlow.findByPurchaseDate", query = "SELECT p FROM PurchaseFlow p WHERE p.purchaseDate = :purchaseDate"),
    @NamedQuery(name = "PurchaseFlow.findByPurchaseClosed", query = "SELECT p FROM PurchaseFlow p WHERE p.purchaseClosed = :purchaseClosed")})
public class PurchaseFlow implements Serializable {
    @JoinColumn(name = "username", referencedColumnName = "username")
    @ManyToOne(optional = false)
    private AppUser appUser;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "purchase_id")
    private Integer purchaseId;
    @Column(name = "manager_approved")
    private Boolean managerApproved;
    @Size(max = 45)
    @Column(name = "purchase_date")
    private String purchaseDate;
    @Column(name = "purchase_closed")
    private Boolean purchaseClosed;
    @JoinColumn(name = "program_id", referencedColumnName = "program_id")
    @ManyToOne(optional = false)
    private Program program;

    public PurchaseFlow() {
    }

    public PurchaseFlow(Integer purchaseId) {
        this.purchaseId = purchaseId;
    }

    public Integer getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Integer purchaseId) {
        this.purchaseId = purchaseId;
    }

    public Boolean getManagerApproved() {
        return managerApproved;
    }

    public void setManagerApproved(Boolean managerApproved) {
        this.managerApproved = managerApproved;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Boolean getPurchaseClosed() {
        return purchaseClosed;
    }

    public void setPurchaseClosed(Boolean purchaseClosed) {
        this.purchaseClosed = purchaseClosed;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (purchaseId != null ? purchaseId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PurchaseFlow)) {
            return false;
        }
        PurchaseFlow other = (PurchaseFlow) object;
        if ((this.purchaseId == null && other.purchaseId != null) || (this.purchaseId != null && !this.purchaseId.equals(other.purchaseId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.licensebox.db.entity.PurchaseFlow[ purchaseId=" + purchaseId + " ]";
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }
    
}