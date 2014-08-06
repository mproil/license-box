package com.licensebox.db.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * This entity represents the app_user table in the database
 * 
 * @author Anna Guzman & Michael Paltsev
 */
@Entity
@Table(name = "app_user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AppUser.findAll", query = "SELECT a FROM AppUser a"),
    @NamedQuery(name = "AppUser.findByUsername", query = "SELECT a FROM AppUser a WHERE a.username = :username"),
    @NamedQuery(name = "AppUser.findByFirstName", query = "SELECT a FROM AppUser a WHERE a.firstName = :firstName"),
    @NamedQuery(name = "AppUser.findByLastName", query = "SELECT a FROM AppUser a WHERE a.lastName = :lastName"),
    @NamedQuery(name = "AppUser.findByEmail", query = "SELECT a FROM AppUser a WHERE a.email = :email"),
    @NamedQuery(name = "AppUser.findByPassword", query = "SELECT a FROM AppUser a WHERE a.password = :password"),
    @NamedQuery(name = "AppUser.findByTeamId", query = "SELECT a FROM AppUser a WHERE a.team = :team"),
    @NamedQuery(name = "AppUser.findUserByRoleName", query = "SELECT a from AppUser a JOIN a.appRoleList r WHERE r.roleName = :roleName")})
public class AppUser implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "appUser")
    private List<PurchaseFlow> purchaseFlowList;
    @JoinTable(name = "user_role", joinColumns = {
        @JoinColumn(name = "username", referencedColumnName = "username")}, inverseJoinColumns = {
        @JoinColumn(name = "role_name", referencedColumnName = "role_name")})
    @ManyToMany
    private List<AppRole> appRoleList;
    @JoinColumn(name = "team_id", referencedColumnName = "team_id")
    @ManyToOne
    private Team team;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "username")
    private String username;
    @Size(max = 45)
    @Column(name = "first_name")
    private String firstName;
    @Size(max = 45)
    @Column(name = "last_name")
    private String lastName;
    @Size(max = 100)
    @Column(name = "email")
    private String email;
    @NotNull
    @Column(name = "ldap")
    private boolean ldap;
    @Basic(optional = false)
//    @NotNull
    @Size(min = 0, max = 64)
    @Column(name = "password")
    private String password;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "appUser")
    private List<LicenseHistory> licenseHistoryList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "appUser")
    private List<License> userLicenses;
    

    public AppUser() {
    }

    public AppUser(String username) {
        this.username = username;
    }

    public AppUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
    
    public boolean isLdapUser() {
        return ldap;
    }
    
    public void setLdapUser(boolean ldap) {
        this.ldap = ldap;
    }

    @XmlTransient
    public List<AppRole> getAppRoleList() {
        return appRoleList;
    }

    public void setAppRoleList(List<AppRole> appRoleList) {
        this.appRoleList = appRoleList;
    }

    @XmlTransient
    public List<LicenseHistory> getLicenseHistoryList() {
        return licenseHistoryList;
    }

    public void setLicenseHistoryList(List<LicenseHistory> licenseHistoryList) {
        this.licenseHistoryList = licenseHistoryList;
    }
    
    @XmlTransient
    public List<License> getUserLicenses() {
        return this.userLicenses;
    }
    
    public void setUserLicenses(List<License> userLicenses) {
        this.userLicenses = userLicenses;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (username != null ? username.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AppUser)) {
            return false;
        }
        AppUser other = (AppUser) object;
        if ((this.username == null && other.username != null) || (this.username != null && !this.username.equals(other.username))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.licensebox.db.entity.AppUser[ username=" + username + " ]";
    }

    @XmlTransient
    public List<PurchaseFlow> getPurchaseFlowList() {
        return purchaseFlowList;
    }

    public void setPurchaseFlowList(List<PurchaseFlow> purchaseFlowList) {
        this.purchaseFlowList = purchaseFlowList;
    }

    
}
