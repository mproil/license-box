package com.licensebox.ui.converter;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 * This converter is responsible for team id conversion
 * 
 * @author Michael Paltsev & Anna Guzman
 */
@ManagedBean
@RequestScoped
public class TeamIdConverter implements Converter, Serializable {

    private static final Long serialVersionUID = 1L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            Integer retVal = new Integer(value);
            return retVal;
        } catch (NumberFormatException ex) {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error", "Please enter a valid decimal number as a team id"));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (!(value instanceof Integer)) {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error", "The value " + value.toString() + " is not an Integer"));
        }
        
        return ((Integer)value).toString();
    }


}
