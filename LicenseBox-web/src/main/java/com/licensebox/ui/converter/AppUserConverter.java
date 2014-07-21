package com.licensebox.ui.converter;

import com.licensebox.db.dao.AppUserDaoLocal;
import com.licensebox.db.entity.AppUser;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * This converter is used to convert an AppUser object to its string representation
 * 
 * @author Michael Paltsev & Anna Guzman
 */
@ManagedBean
@RequestScoped
public class AppUserConverter implements Converter, Serializable {

    private static final Long serialVersionUID = 1L;
    
    @EJB
    AppUserDaoLocal appUserDao;

    @PostConstruct
    public void init() {
        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return (Object)value;
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
        if (value instanceof AppUser) {
            ans = ((AppUser)value).getUsername();
        } else if (value instanceof String) {
            ans = (String)value;
        }
        return ans;
    }


}
