package com.licensebox.ui.page.teamleader;

import com.licensebox.db.dao.LicenseDaoLocal;
import com.licensebox.db.dao.TeamDaoLocal;
import com.licensebox.db.entity.License;
import com.licensebox.db.entity.Team;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 * This Managed Bean acts as the controller of the reports view for the team leader
 * 
 * @author Michael Paltsev & Anna Guzman
 */
@ManagedBean
@ViewScoped
public class TeamLeaderReportsManagedBean implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="EJB Injection">
    @EJB
    private LicenseDaoLocal licenseDao;
    
    @EJB
    private TeamDaoLocal teamDao;
    //</editor-fold>
       
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private CartesianChartModel licensesPerTeamMember;
    private int licensesPerTeamMaxVal;
    private final double OVER_HEAD_MULTIPLIER = 1.3;
    
    private String teamLeaderUsername;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Getters & Setters">
    public CartesianChartModel getLicensesPerTeamMember() {
        return licensesPerTeamMember;
    }

    public int getLicensesPerTeamMaxVal() {
        return licensesPerTeamMaxVal;
    }
    //</editor-fold>
    
    @PostConstruct
    public void init() {
        this.teamLeaderUsername = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        List<License> tempLicenses = this.licenseDao.getAll();
        List<Team> teamList  = this.teamDao.getTeamsByTeamManager(teamLeaderUsername);
        List<License> licenses = getLicensesByTeamList(teamList, tempLicenses);
        
        this.licensesPerTeamMaxVal = 0;
        generateDataForPurchaseByTeamMember(licenses, teamList);
    }
    
    private void generateDataForPurchaseByTeamMember(List<License> licenses, List<Team> teams) {
        if ((licenses != null) && (!licenses.isEmpty()) && (teams != null) && (!teams.isEmpty())) {
            Map<String, Integer> valuesMap = new HashMap<>();
            String teamName;
            
            for (License license : licenses) {
                if (license.getAppUser() != null) {
                    if (teams.contains(license.getAppUser().getTeam())) {
                        teamName = (license.getAppUser().getTeam().getTeamName() != null) ? 
                                license.getAppUser().getTeam().getTeamName() : "Unknown Team";
                    
                        if (valuesMap.containsKey(teamName)) {
                            Integer prevValue = valuesMap.get(teamName);
                            valuesMap.put(teamName, ++prevValue);
                        } else {
                            valuesMap.put(teamName, 1);
                        }
                    }
                }
            }
            
            ChartSeries chartSeries = new ChartSeries("Num. Of Licenses");
            for (String key : valuesMap.keySet()) {
                this.licensesPerTeamMaxVal = (this.licensesPerTeamMaxVal < valuesMap.get(key)) ?
                        valuesMap.get(key) : this.licensesPerTeamMaxVal;
                chartSeries.set(key, valuesMap.get(key));
            }
            
            this.licensesPerTeamMember = new CartesianChartModel();
            this.licensesPerTeamMember.addSeries(chartSeries);
            
            this.licensesPerTeamMaxVal = (int)(this.licensesPerTeamMaxVal * OVER_HEAD_MULTIPLIER);
        }
    }
    
    private List<License> getLicensesByTeamList(List<Team> teamList, List<License> licenseList) {
        List<License> retVal = new LinkedList<>();
        
        if (licenseList != null) {
            for (License license : licenseList) {
                if ((license.getAppUser() != null) && (license.getAppUser().getTeam() != null)) {
                    if (teamList.contains(license.getAppUser().getTeam())) {
                        retVal.add(license);
                    }
                }
            }
        }
        
        return retVal;
    }
    
}