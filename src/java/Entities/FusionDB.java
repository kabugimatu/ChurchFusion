/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author kabugi
 */
@Entity
public class FusionDB implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String maxisCapable ; //status
    @Temporal(TemporalType.DATE)
    private Date maxisLaunch; //install date
    private int countMaxi1; //Days active
    private String maxisLoc ; //Locationt
    private String maxisBapt; //SaccoName
    private String maxisCall; //tel
    private String maxisEnc; //Service code

    public String getMaxisEnc() {
        return maxisEnc;
    }

    public void setMaxisEnc(String maxisEnc) {
        this.maxisEnc = maxisEnc;
    }

   
    
    
    

    public String getMaxisLoc() {
        return maxisLoc;
    }

    public void setMaxisLoc(String maxisLoc) {
        this.maxisLoc = maxisLoc;
    }

    public String getMaxisBapt() {
        return maxisBapt;
    }

    public void setMaxisBapt(String maxisBapt) {
        this.maxisBapt = maxisBapt;
    }

    public String getMaxisCall() {
        return maxisCall;
    }

    public void setMaxisCall(String maxisCall) {
        this.maxisCall = maxisCall;
    }
    
    
    
    
    public String getMaxisCapable() {
        return maxisCapable;
    }

    public void setMaxisCapable(String maxisCapable) {
        this.maxisCapable = maxisCapable;
    }

    public Date getMaxisLaunch() {
        return maxisLaunch;
    }

    public void setMaxisLaunch(Date maxisLaunch) {
        this.maxisLaunch = maxisLaunch;
    }

    public int getCountMaxi1() {
        return countMaxi1;
    }

    public void setCountMaxi1(int countMaxi1) {
        this.countMaxi1 = countMaxi1;
    }
    
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FusionDB)) {
            return false;
        }
        FusionDB other = (FusionDB) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.FusionDB[ id=" + id + " ]";
    }
    
}
