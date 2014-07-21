package com.licensebox.ui.page.licenseadmin;

import com.licensebox.db.dao.LicenseDaoLocal;
import com.licensebox.db.dao.ProgramDaoLocal;
import com.licensebox.db.entity.License;
import com.licensebox.db.entity.Program;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 * This Managed Bean acts as the controller for the reports page of the license
 * manager
 * 
 * @author Michael Paltsev & Anna Guzman
 */
@ManagedBean
@ViewScoped
public class LicenseManagerReportsManagedBean implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="EJB Injection">
    @EJB
    private LicenseDaoLocal licenseDao;
    
    @EJB
    private ProgramDaoLocal programDao;
    //</editor-fold>
       
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private CartesianChartModel licensesPerProgram;
    private int licensesPerProgramMaxVal;
    private final double OVER_HEAD_MULTIPLIER = 1.3;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Getters & Setters">
    public CartesianChartModel getLicensesPerProgram() {
        return licensesPerProgram;
    }

    public int getLicensesPerProgramMaxVal() {
        return licensesPerProgramMaxVal;
    }
    //</editor-fold>
    
    @PostConstruct
    public void init() {
        List<Program> programs = this.programDao.getAll();
        
        this.licensesPerProgramMaxVal = 0;
        generateDataForPurchaseByProgram(programs);
    }
    
    
    /**
     * This generates the data for the report that shows purchases by program
     * @param programs 
     */
    private void generateDataForPurchaseByProgram(List<Program> programs) {
        if (programs != null) {
            Map<String, Integer> valuesMap = new HashMap<>();
            String programName;
            Integer numOfLicenses;
            
            for (Program program : programs) {
                programName = program.getProgramName() + " " + program.getVersion();
                List<License> tempLicenseList = this.licenseDao.getLicensesByProgram(program);
                if (tempLicenseList != null) {
                    numOfLicenses = tempLicenseList.size();
                } else {
                    numOfLicenses = 0;
                }
                valuesMap.put(programName, numOfLicenses);
            }
            
            ChartSeries chartSeries = new ChartSeries("Num. Of Licenses");
            for (String key : valuesMap.keySet()) {
                this.licensesPerProgramMaxVal = (this.licensesPerProgramMaxVal < valuesMap.get(key)) ?
                        valuesMap.get(key) : this.licensesPerProgramMaxVal;
                chartSeries.set(key, valuesMap.get(key));
            }
            
            this.licensesPerProgram = new CartesianChartModel();
            this.licensesPerProgram.addSeries(chartSeries);
            
            this.licensesPerProgramMaxVal = (int)(this.licensesPerProgramMaxVal * OVER_HEAD_MULTIPLIER);
        }
    }
    
}