package com.licensebox.db.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This entity represents the license table in the database
 * 
 * @author Anna Guzman & Michael Paltsev
 */
@Entity
@Table(name = "license")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "License.findAll", query = "SELECT l FROM License l"),
    @NamedQuery(name = "License.findByLicenseId", query = "SELECT l FROM License l WHERE l.licenseId = :licenseId"),
    @NamedQuery(name = "License.findByPrice", query = "SELECT l FROM License l WHERE l.price = :price"),
    @NamedQuery(name = "License.findByPurchaseDate", query = "SELECT l FROM License l WHERE l.purchaseDate = :purchaseDate"),
    @NamedQuery(name = "License.findByUpgradeDate", query = "SELECT l FROM License l WHERE l.upgradeDate = :upgradeDate"),
    @NamedQuery(name = "License.findByProgram", query = "SELECT l FROM License l WHERE l.program = :program"),
    @NamedQuery(name = "License.findFreeByProgram", query = "SELECT l FROM License l WHERE l.program = :program AND l.appUser IS NULL"),
    @NamedQuery(name = "License.findByUser", query = "SELECT l FROM License l WHERE l.appUser = :appUser")})
public class License implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "license_id")
    private String licenseId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "price")
    private BigDecimal price;
    @Basic(optional = false)
    @NotNull
    @Column(name = "purchase_date")
    @Temporal(TemporalType.DATE)
    private Date purchaseDate;
    @Column(name = "upgrade_date")
    @Temporal(TemporalType.DATE)
    private Date upgradeDate;
    @Lob
    @Column(name = "attachement")
    private byte[] attachement;
    @ManyToOne(optional = false)
    @JoinColumn(name = "program_id", referencedColumnName = "program_id")
    private Program program;
    @ManyToOne(optional = true)
    @JoinColumn(name = "username", referencedColumnName = "username")
    private AppUser appUser;

    public License() {
    }

    public License(String licenseId) {
        this.licenseId = licenseId;
    }

    public License(String licenseId, BigDecimal price, Date purchaseDate, Program program) {
        this.licenseId = licenseId;
        this.price = price;
        this.purchaseDate = purchaseDate;
        this.program = program;
    }

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Date getUpgradeDate() {
        return upgradeDate;
    }

    public void setUpgradeDate(Date upgradeDate) {
        this.upgradeDate = upgradeDate;
    }

    public byte[] getAttachement() {
        return attachement;
    }

    public void setAttachement(byte[] attachement) {
        this.attachement = attachement;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }
    
    public AppUser getAppUser() {
        return this.appUser;
    }
    
    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (licenseId != null ? licenseId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof License)) {
            return false;
        }
        License other = (License) object;
        if ((this.licenseId == null && other.licenseId != null) || (this.licenseId != null && !this.licenseId.equals(other.licenseId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.licensebox.db.entity.License[ licenseId=" + licenseId + " ]";
    }
    
}