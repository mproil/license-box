package com.licensebox.db.entity;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This entity represents the license_flow table in the database
 * 
 * @author Anna Guzman & Michael Paltsev
 */
@Entity
@Table(name = "license_flow")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LicenseFlow.findAll", query = "SELECT l FROM LicenseFlow l"),
    @NamedQuery(name = "LicenseFlow.findByRequestId", query = "SELECT l FROM LicenseFlow l WHERE l.requestId = :requestId"),
    @NamedQuery(name = "LicenseFlow.findByUsername", query = "SELECT l FROM LicenseFlow l WHERE l.appUser.username = :username"),
    @NamedQuery(name = "LicenseFlow.findByTeamleadApproved", query = "SELECT l FROM LicenseFlow l WHERE l.teamleadApproved = :teamleadApproved"),
    @NamedQuery(name = "LicenseFlow.findByLicmanApproved", query = "SELECT l FROM LicenseFlow l WHERE l.licmanApproved = :licmanApproved AND l.teamleadApproved = :teamleadApproved"),
    @NamedQuery(name = "LicenseFlow.findByRequestDate", query = "SELECT l FROM LicenseFlow l WHERE l.requestDate = :requestDate")})
public class LicenseFlow implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "request_id")
    private Integer requestId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "teamlead_approved")
    private boolean teamleadApproved;
    @Basic(optional = false)
    @NotNull
    @Column(name = "licman_approved")
    private boolean licmanApproved;
    @Basic(optional = false)
    @NotNull
    @Column(name = "request_date")
    @Temporal(TemporalType.DATE)
    private Date requestDate;
    @JoinColumn(name = "program_id", referencedColumnName = "program_id")
    @ManyToOne(optional = false)
    private Program program;
    @JoinColumn(name = "username", referencedColumnName = "username")
    @ManyToOne(optional = false)
    private AppUser appUser;

    public LicenseFlow() {
    }

    public LicenseFlow(Integer requestId) {
        this.requestId = requestId;
    }
    
    public LicenseFlow(AppUser appUser, Program program){
        this.appUser = appUser;
        this.program = program;
        this.teamleadApproved = false;
        this.licmanApproved = false;
        this.requestDate = new Date();
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public boolean getTeamleadApproved() {
        return teamleadApproved;
    }

    public void setTeamleadApproved(boolean approved) {
        this.teamleadApproved = approved;
    }

    public boolean getLicmanApproved() {
        return licmanApproved;
    }

    public void setLicmanApproved(boolean approved) {
        this.licmanApproved = approved;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (requestId != null ? requestId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LicenseFlow)) {
            return false;
        }
        LicenseFlow other = (LicenseFlow) object;
        if ((this.requestId == null && other.requestId != null) || (this.requestId != null && !this.requestId.equals(other.requestId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.licensebox.db.entity.LicenseFlow[ requestId=" + requestId + " ]";
    }
    
}
