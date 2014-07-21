package com.licensebox.db.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * This entity represents the app_role table in the database
 * 
 * @author Michael Paltsev & Anna Guzman
 */
@Entity
@Table(name = "app_role")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AppRole.findAll", query = "SELECT a FROM AppRole a"),
    @NamedQuery(name = "AppRole.findByRoleName", query = "SELECT a FROM AppRole a WHERE a.roleName = :roleName"),
    @NamedQuery(name = "AppRole.findByRoleDesc", query = "SELECT a FROM AppRole a WHERE a.roleDesc = :roleDesc")})
    public class AppRole implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "role_name")
    private String roleName;
    @Size(max = 200)
    @Column(name = "role_desc")
    private String roleDesc;
    @JoinTable(name = "user_role", joinColumns = {
        @JoinColumn(name = "role_name", referencedColumnName = "role_name")}, inverseJoinColumns = {
        @JoinColumn(name = "username", referencedColumnName = "username")})
    @ManyToMany
    private List<AppUser> appUserList;

    public AppRole() {
    }

    public AppRole(String roleName) {
        this.roleName = roleName;
    }

    public RoleName getRoleName() {
        return RoleName.parse(roleName);
    }

    public void setRoleName(RoleName roleName) {
        this.roleName = roleName.getValue();
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    @XmlTransient
    public List<AppUser> getAppUserList() {
        return appUserList;
    }

    public void setAppUserList(List<AppUser> appUserList) {
        this.appUserList = appUserList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roleName != null ? roleName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AppRole)) {
            return false;
        }
        AppRole other = (AppRole) object;
        if ((this.roleName == null && other.roleName != null) || (this.roleName != null && !this.roleName.equals(other.roleName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.licensebox.db.entity.AppRole[ roleName=" + roleName + " ]";
    }
    
    public enum RoleName {
        ADMIN("ADMIN"),
        USER("USER"),
        LICENSE_MANAGER("LICENSE_MANAGER"),
        TEAM_LEADER("TEAM_LEADER"),
        MANAGER("MANAGER");
        
        private String value;
        
        RoleName(String roleName) {
            this.value = roleName;
        }
        
        public String getValue() {
            return this.value;
        }
        
        public static RoleName parse(String id) {
            RoleName ans = null;
            for (RoleName roleNameItem : RoleName.values()) {
                if (roleNameItem.getValue().equals(id)) {
                    ans = roleNameItem;
                    break;
                }
            }
            return ans;
        }
    }

    
    
}
