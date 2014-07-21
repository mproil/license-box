package com.licensebox.ui.page.administration;

import com.licensebox.ui.Helper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;

/**
 * This Managed Bean is the controller of the view logsAdmin
 * 
 * @author Michael Paltsev & Anna Guzman
 */
@ManagedBean
@ViewScoped
public class LogsManagedBean implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="Properties">
    private Logger logger = Logger.getLogger(LogsManagedBean.class);
    private LinkedList<String> logData;
    private static final String LOG_FILE = System.getProperty("com.sun.aas.instanceRoot") + File.separator + "logs" +
            File.separator + "licensebox.log";
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Getters & Setters">
    public List<String> getLogData() {
        return this.logData;
    }
    //</editor-fold>
    
    /**
     * The constructor of this class. It is used to initialize all the components.
     */
    public LogsManagedBean() {
        this.logData = new LinkedList<>();
        try (FileReader fileReader = new FileReader(new File(LOG_FILE))) {
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    this.logData.addFirst(line);
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            Helper.addFacesMessage(Helper.ERROR, ex.getMessage());
        }
    }

}
