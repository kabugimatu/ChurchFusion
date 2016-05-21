
package Entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author george
 */
@Entity
public class MemberSettings implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private double benevolentFee=0.0;
    private double minimumShares=0.0;
    private double minimumDeposit=0.0; 
    private double salaryLimit;
    private int maximumReferees=0;
    private String editor;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date lastUpdate;

    public double getBenevolentFee() {
        return benevolentFee;
    }

    public void setBenevolentFee(double benevolentFee) {
        this.benevolentFee = benevolentFee;
    }

    public double getSalaryLimit() {
        return salaryLimit;
    }

    public void setSalaryLimit(double salaryLimit) {
        this.salaryLimit = salaryLimit;
    }
    

  

    public double getMinimumShares() {
        return minimumShares;
    }

    public void setMinimumShares(double minimumShares) {
        this.minimumShares = minimumShares;
    }

    public double getMinimumDeposit() {
        return minimumDeposit;
    }

    public void setMinimumDeposit(double minimumDeposit) {
        this.minimumDeposit = minimumDeposit;
    }

    public int getMaximumReferees() {
        return maximumReferees;
    }

    public void setMaximumReferees(int maximumReferees) {
        this.maximumReferees = maximumReferees;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
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
        if (!(object instanceof MemberSettings)) {
            return false;
        }
        MemberSettings other = (MemberSettings) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.MemberSettings[ id=" + id + " ]";
    }
    
}
