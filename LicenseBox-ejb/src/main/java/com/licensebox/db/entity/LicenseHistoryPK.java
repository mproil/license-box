package com.licensebox.db.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * This entity represents the primary key of the license_history table
 * 
 * @author Anna Guzman & Michael Paltsev
 */
@Embeddable
public class LicenseHistoryPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "license_id")
    private String licenseId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    public LicenseHistoryPK() {
    }

    public LicenseHistoryPK(String licenseId, String username, Date startDate) {
        this.licenseId = licenseId;
        this.username = username;
        this.startDate = startDate;
    }

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (licenseId != null ? licenseId.hashCode() : 0);
        hash += (username != null ? username.hashCode() : 0);
        hash += (startDate != null ? startDate.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LicenseHistoryPK)) {
            return false;
        }
        LicenseHistoryPK other = (LicenseHistoryPK) object;
        if ((this.licenseId == null && other.licenseId != null) || (this.licenseId != null && !this.licenseId.equals(other.licenseId))) {
            return false;
        }
        if ((this.username == null && other.username != null) || (this.username != null && !this.username.equals(other.username))) {
            return false;
        }
        if ((this.startDate == null && other.startDate != null) || (this.startDate != null && !this.startDate.equals(other.startDate))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.licensebox.db.entity.LicenseHistoryPK[ licenseId=" + licenseId + ", username=" + username + ", startDate=" + startDate + " ]";
    }
    
}