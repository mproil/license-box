package com.licensebox.ui.converter;

import com.licensebox.db.dao.ProgramDaoLocal;
import com.licensebox.db.entity.Program;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * This class is responsible for the conversion between Program
 * and a String representing the Program
 * 
 * @author Michael Paltsev & Anna Guzman
 */
@ManagedBean
@RequestScoped
public class ProgramConverter implements Converter, Serializable {

    private static final Long serialVersionUID = 1L;
    
    @EJB
    ProgramDaoLocal programDao;
    
    @PostConstruct
    public void init() {
        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        String[] tempStringArray = value.split(" ");
        List<Program> tempProgramList = this.programDao.getAll();
        Program retVal = null;
        if (tempStringArray.length == 2) {
            for (Program program : tempProgramList) {
                if ((program.getProgramName().equals(tempStringArray[0])) &&
                        (program.getVersion().equals(tempStringArray[1]))) {
                    retVal = program;
                    break;
                }
            }
        }
        return (Object)retVal;
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
            ans = this.programDao.getByProgramId((Integer)value).getProgramName();
        } else if (value instanceof Program) {
            ans = ((Program)value).getProgramName() + " " + ((Program)value).getVersion();
        }
        
        return ans;
    }
}
