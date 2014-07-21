package com.licensebox.db.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * This entity represents the program table in the database
 * 
 * @author Anna Guzman & Michael Paltsev
 */
@Entity
@Table(name = "program")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Program.findAll", query = "SELECT p FROM Program p"),
    @NamedQuery(name = "Program.findByProgramId", query = "SELECT p FROM Program p WHERE p.programId = :programId"),
    @NamedQuery(name = "Program.findByProgramName", query = "SELECT p FROM Program p WHERE p.programName = :programName"),
    @NamedQuery(name = "Program.findBySiteLink", query = "SELECT p FROM Program p WHERE p.siteLink = :siteLink"),
    @NamedQuery(name = "Program.findByVersion", query = "SELECT p FROM Program p WHERE p.version = :version")})
public class Program implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "program")
    private List<PurchaseFlow> purchaseFlowList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "program")
    private List<LicenseFlow> licenseFlowList;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "program_id")
    private Integer programId;
    @Size(max = 45)
    @Column(name = "program_name")
    private String programName;
    @Size(max = 100)
    @Column(name = "site_link")
    private String siteLink;
    @Size(max = 45)
    @Column(name = "version")
    private String version;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "program")
    private List<License> licenseList;

    public Program() {
    }

    public Program(Integer programId) {
        this.programId = programId;
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

    public String getSiteLink() {
        return siteLink;
    }

    public void setSiteLink(String siteLink) {
        this.siteLink = siteLink;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @XmlTransient
    public List<License> getLicenseList() {
        return licenseList;
    }

    public void setLicenseList(List<License> licenseList) {
        this.licenseList = licenseList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (programId != null ? programId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Program)) {
            return false;
        }
        Program other = (Program) object;
        if ((this.programId == null && other.programId != null) || (this.programId != null && !this.programId.equals(other.programId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.licensebox.db.entity.Program[ programId=" + programId + " ]";
    }

    @XmlTransient
    public List<LicenseFlow> getLicenseFlowList() {
        return licenseFlowList;
    }

    public void setLicenseFlowList(List<LicenseFlow> licenseFlowList) {
        this.licenseFlowList = licenseFlowList;
    }

    @XmlTransient
    public List<PurchaseFlow> getPurchaseFlowList() {
        return purchaseFlowList;
    }

    public void setPurchaseFlowList(List<PurchaseFlow> purchaseFlowList) {
        this.purchaseFlowList = purchaseFlowList;
    }
    
}
