package com.licensebox.ui.page.manager;

import com.licensebox.db.dao.LicenseDaoLocal;
import com.licensebox.db.entity.License;
import com.licensebox.ui.Helper;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
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
 * This Managed Bean is used as a controller for the reports page for the manager
 * @author Michael Paltsev & Anna Guzman
 */
@ManagedBean
@ViewScoped
public class ManagerReportsManagedBean implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="EJB Injection">
    @EJB
    private LicenseDaoLocal licenseDao;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private CartesianChartModel purchasesByYear;
    private CartesianChartModel licensesPerTeam;
    private int purchasesByYearMaxVal;
    private int licensesPerTeamMaxVal;
    private final double OVER_HEAD_MULTIPLIER = 1.3;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters & Setters">
    public CartesianChartModel getPurchasesByYear() {
        return purchasesByYear;
    }
    
    public CartesianChartModel getLicensesPerTeam() {
        return licensesPerTeam;
    }
    
    public int getPurchasesByYearMaxVal() {
        return purchasesByYearMaxVal;
    }
    
    public int getLicensesPerTeamMaxVal() {
        return licensesPerTeamMaxVal;
    }
    //</editor-fold>
    
    @PostConstruct
    public void init() {
        List<License> licenses = this.licenseDao.getAll();
        
        this.purchasesByYearMaxVal = 0;
        generateDataForPurchasesByYear(licenses);
        
        this.licensesPerTeamMaxVal = 0;
        generateDataForPurchaseByTeam(licenses);
    }
    
    private void generateDataForPurchasesByYear(List<License> licenses) {
        if (licenses != null) {
            Map<Integer, BigDecimal> valuesMap = new HashMap<>();
            Integer year;
            BigDecimal totalForYear;
            
            for (License license : licenses) {
                year = Helper.dateToCalendar(license.getPurchaseDate()).get(Calendar.YEAR);
                totalForYear = license.getPrice();
                
                if (valuesMap.containsKey(year)) {
                    BigDecimal prevValue = valuesMap.get(year);
                    valuesMap.put(year, totalForYear.add(prevValue));
                } else {
                    valuesMap.put(year, totalForYear);
                }
            }
            
            List<Map.Entry<Integer, BigDecimal>> sortedValuesList = Helper.sortMapByKey(valuesMap);
            
            ChartSeries chartSeries = new ChartSeries("Total Cost");
            
            for (Map.Entry<Integer, BigDecimal> entry : sortedValuesList) {
                this.purchasesByYearMaxVal = (this.purchasesByYearMaxVal < entry.getValue().intValue()) ?
                        entry.getValue().intValue() : this.purchasesByYearMaxVal;
                chartSeries.set(entry.getKey().toString(), entry.getValue());
            }
            
            // Increase the max value just a bit
            this.purchasesByYearMaxVal = (int)(this.purchasesByYearMaxVal * OVER_HEAD_MULTIPLIER);
            
            this.purchasesByYear = new CartesianChartModel();
            this.purchasesByYear.addSeries(chartSeries);
        }
    }
    
    private void generateDataForPurchaseByTeam(List<License> licenses) {
        if (licenses != null) {
            Map<String, Integer> valuesMap = new HashMap<>();
            String teamName;
            
            for (License license : licenses) {
                if (license.getAppUser() != null) {
                    if (license.getAppUser().getTeam() != null) {
                        teamName = license.getAppUser().getTeam().getTeamName();
                    } else {
                        teamName = "Unknown Team";
                    }
                    
                    if (valuesMap.containsKey(teamName)) {
                        Integer prevValue = valuesMap.get(teamName);
                        valuesMap.put(teamName, ++prevValue);
                    } else {
                        valuesMap.put(teamName, 1);
                    }
                }
            }
            
            if (!valuesMap.isEmpty()) {
                ChartSeries chartSeries = new ChartSeries("Num. Of Licenses");
                for (String key : valuesMap.keySet()) {
                    this.licensesPerTeamMaxVal = (this.licensesPerTeamMaxVal < valuesMap.get(key)) ?
                            valuesMap.get(key) : this.licensesPerTeamMaxVal;
                    chartSeries.set(key, valuesMap.get(key));
                }

                this.licensesPerTeam = new CartesianChartModel();
                this.licensesPerTeam.addSeries(chartSeries);

                this.licensesPerTeamMaxVal = (int)(this.licensesPerTeamMaxVal * OVER_HEAD_MULTIPLIER);
            }
        }
    }
    
}
