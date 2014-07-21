package com.licensebox.ui;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * This POJO contains static methods that are in use in several Managed Beans
 * in the project
 * 
 * @author Michael Paltsev & Anna Guzman
 */
public class Helper {
    
    /**
     * Info code used in the addFacesMessage method
     */
    public static final int INFO = 0;
    
    /**
     * Error code used in the addFacesMessage method
     */
    public static final int ERROR = 1;
    
    /**
     * This method adds a FacesMessage to the context
     * @param severity The severity value indicated by INFO\ERROR static values
     * @param message The message that the user wishes to display
     */
    public static void addFacesMessage(int severity, String message) {
        
        FacesMessage facesMessage;
        
        switch (severity) {
            case INFO:
                facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", message);
                break;
            case ERROR:
                facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", message);                
                break;
            default:
                facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Unknown", message);
                break;
        }
        
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
    }

    /**
     * This method is responsible for sorting a given map by its key values and
     * returning a sorted List containing the entries of the map
     * @param <K> The type of the Key
     * @param <V> The type of the Value
     * @param mapToSort The Map you wish to sort
     * @return A List containing the map entries in a sorted order
     */
    public static <K extends Comparable<? super K>, V> List<Map.Entry<K, V>> sortMapByKey(Map<K, V> mapToSort) {
        List<Map.Entry<K, V>> retVal = new LinkedList<>(mapToSort.entrySet());
        Collections.sort(retVal, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        return retVal;
    }

    /**
     * Transforms a date object into a Calendar object
     * @param date The Date object you want to transform
     * @return A Calendar object
     */
    public static Calendar dateToCalendar(Date date) {
        Calendar retVal = Calendar.getInstance();
        retVal.setTime(date);
        return retVal;
    }
    
}
