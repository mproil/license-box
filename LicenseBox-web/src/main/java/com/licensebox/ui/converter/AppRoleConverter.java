package com.licensebox.ui.converter;

import com.licensebox.db.dao.AppRoleDaoLocal;
import com.licensebox.db.entity.AppRole;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * A converter that is used to convert the application roles
 * @author Michael Paltsev & Anna Guzman
 */
@ManagedBean
@RequestScoped
public class AppRoleConverter implements Converter, Serializable {

    private static final Long serialVersionUID = 1L;
    
    @EJB
    AppRoleDaoLocal appRoleDao;

    @PostConstruct
    public void init() {
        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return (Object)this.appRoleDao.findRole(AppRole.RoleName.valueOf(value));
    }

    /**
     * {@inheritDoc}
     * @param context
     * @param component
     * @param value
     * @return 
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String ans = "";
        if (value instanceof AppRole.RoleName) {
            ans = ((AppRole.RoleName)value).getValue();
        } else if (value instanceof AppRole) {
            ans = ((AppRole)value).getRoleName().getValue();
        }
        return ans;
    }


}
