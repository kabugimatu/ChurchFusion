/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SupportBeans;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author kabugi & george - PearlSoft Technologies
 */
public class SaccoLicense implements Serializable{
    private String saccoName;
    private Date installationDate;
    private boolean trialVersion;
    private boolean licensed;
    private String serviceCode;

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }
    
    

    public String getSaccoName() {
        return saccoName;
    }

    public void setSaccoName(String saccoName) {
        this.saccoName = saccoName;
    }

    public Date getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(Date installationDate) {
        this.installationDate = installationDate;
    }

    public boolean isTrialVersion() {
        return trialVersion;
    }

    public void setTrialVersion(boolean trialVersion) {
        this.trialVersion = trialVersion;
    }

    public boolean isLicensed() {
        return licensed;
    }

    public void setLicensed(boolean licensed) {
        this.licensed = licensed;
    }
    
    
    
    
    

}
