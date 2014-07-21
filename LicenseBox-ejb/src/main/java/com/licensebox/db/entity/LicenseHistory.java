package com.licensebox.db.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This entity represents the license_history table in the database
 * 
 * @author Anna Guzman & Michael Paltsev
 */@Entity
@Table(name = "license_history")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LicenseHistory.findAll", query = "SELECT l FROM LicenseHistory l"),
    @NamedQuery(name = "LicenseHistory.findByLicenseId", query = "SELECT l FROM LicenseHistory l WHERE l.licenseHistoryPK.licenseId = :licenseId"),
    @NamedQuery(name = "LicenseHistory.findByUsername", query = "SELECT l FROM LicenseHistory l WHERE l.licenseHistoryPK.username = :username"),
    @NamedQuery(name = "LicenseHistory.findByStartDate", query = "SELECT l FROM LicenseHistory l WHERE l.licenseHistoryPK.startDate = :startDate"),
    @NamedQuery(name = "LicenseHistory.findCurrentByUsername", query = "SELECT l FROM LicenseHistory l WHERE l.licenseHistoryPK.username = :username AND l.endDate > :currDate"),
    @NamedQuery(name = "LicenseHistory.findByEndDate", query = "SELECT l FROM LicenseHistory l WHERE l.endDate = :endDate"),
    @NamedQuery(name = "LicenseHistory.findLastFreeByLicenseId", query = "SELECT l FROM LicenseHistory l WHERE l.licenseHistoryPK.licenseId = :licenseId AND l.endDate IS NULL")})
public class LicenseHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected LicenseHistoryPK licenseHistoryPK;
    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @JoinColumn(name = "username", referencedColumnName = "username", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private AppUser appUser;

    public LicenseHistory() {
    }

    public LicenseHistory(LicenseHistoryPK licenseHistoryPK) {
        this.licenseHistoryPK = licenseHistoryPK;
    }

    public LicenseHistory(LicenseHistoryPK licenseHistoryPK, String licenseId1) {
        this.licenseHistoryPK = licenseHistoryPK;
//        this.licenseId1 = licenseId1;
    }

    public LicenseHistory(String licenseId, String username, Date startDate) {
        this.licenseHistoryPK = new LicenseHistoryPK(licenseId, username, startDate);
    }

    public LicenseHistoryPK getLicenseHistoryPK() {
        return licenseHistoryPK;
    }

    public void setLicenseHistoryPK(LicenseHistoryPK licenseHistoryPK) {
        this.licenseHistoryPK = licenseHistoryPK;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    
//    public String getLicenseId1() {
//        return licenseId1;
//    }
//
//    public void setLicenseId1(String licenseId1) {
//        this.licenseId1 = licenseId1;
//    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (licenseHistoryPK != null ? licenseHistoryPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LicenseHistory)) {
            return false;
        }
        LicenseHistory other = (LicenseHistory) object;
        if ((this.licenseHistoryPK == null && other.licenseHistoryPK != null) || (this.licenseHistoryPK != null && !this.licenseHistoryPK.equals(other.licenseHistoryPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.licensebox.db.entity.LicenseHistory[ licenseHistoryPK=" + licenseHistoryPK + " ]";
    }
    
}
