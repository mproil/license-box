package com.licensebox.ui.converter;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 * This converter is responsible for price conversion
 * 
 * @author Michael Paltsev & Anna Guzman
 */
@ManagedBean
@RequestScoped
public class PriceConverter implements Converter, Serializable {

    private static final Long serialVersionUID = 1L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            BigDecimal retVal = new BigDecimal(value);
            return retVal;
        } catch (NumberFormatException ex) {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error", "Please enter a valid decimal number as a price"));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (!(value instanceof BigDecimal)) {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error", "The value " + value.toString() + " is not a BigDecimal"));
        }
        
        return ((BigDecimal)value).toString();
    }


}
