package com.licensebox.ui;

import java.security.Principal;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author mpaltsev
 */
@ManagedBean
@ViewScoped
public class LoginManagedBean {

    private String username;
    
    private String password;

    /**
     * Get the value of password
     *
     * @return the value of password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the value of password
     *
     * @param password new value of password
     */
    public void setPassword(String password) {
        this.password = password;
    }


    /**
     * Get the value of username
     *
     * @return the value of username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the value of username
     *
     * @param username new value of username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public Object doLogin() {
        String result = null;
        FacesContext context = FacesContext.getCurrentInstance();
        
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        
        try {
            Principal principal = request.getUserPrincipal();
            if ((principal == null) || !(principal.getName().equals(this.username))) {
                request.login(this.username, this.password);
            }
            result = "app/index";
        } catch (ServletException ex) {
            this.password = null;
            Helper.addFacesMessage(Helper.ERROR, ex.getMessage());
        }
        
        return result;
    }
    
}
