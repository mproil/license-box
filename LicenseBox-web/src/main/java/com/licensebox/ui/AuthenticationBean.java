package com.licensebox.ui;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;



/**
 * This Managed Bean handles the authentication part of the project
 * 
 * @author Michael Paltsev & Anna Guzman
 */
@ManagedBean
@RequestScoped
public class AuthenticationBean {
    
    private Logger logger = Logger.getLogger(AuthenticationBean.class);

    /**
     * Creates a new instance of AuthenticationBean
     */
    public AuthenticationBean() {
    }
    
    /**
     * This method logs the currently connected user out from the web application
     * @return 
     */
    public String logout() {
        String result = "/login?faces-redirect=true";
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        
        String username = facesContext.getExternalContext().getRemoteUser();
        
        facesContext.getExternalContext().invalidateSession();
        HttpServletRequest request = (HttpServletRequest)facesContext.getExternalContext().getRequest();
        
        try {
            request.logout();
        } catch (ServletException ex) {
            result = "/loginError?faces-redirect=true";
        }
        
        logger.info(username + " has logged out from LicenseBox");
        
        return result;
    }
}
