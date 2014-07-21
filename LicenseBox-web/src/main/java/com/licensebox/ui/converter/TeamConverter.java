package com.licensebox.ui.converter;

import com.licensebox.db.dao.TeamDaoLocal;
import com.licensebox.db.entity.Team;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * This class is responsible for the conversion between Team (id, name)
 * and a String representing the Team
 * 
 * @author Michael Paltsev & Anna Guzman
 */
@ManagedBean
@RequestScoped
public class TeamConverter implements Converter, Serializable {

    private static final Long serialVersionUID = 1L;
    
    @EJB
    TeamDaoLocal teamDao;
    
    @PostConstruct
    public void init() {
        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value.matches("\\d+")) {
            return (Object)this.teamDao.getTeamById(Integer.valueOf(value).intValue());
        }
        return (Object)this.teamDao.getTeamByName(value);
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String ans = "";
        if (value instanceof String) {
            ans = (String)value;
        } else if (value instanceof Integer) {
            ans = this.teamDao.getTeamById((Integer)value).getTeamName();
        } else if (value instanceof Team) {
            ans = ((Team)value).getTeamName();
        }
        
        return ans;
    }
}
