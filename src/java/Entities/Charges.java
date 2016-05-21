
package Entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author George / Kabugi Pearl Soft Technologies 
 * All rights reserved
 * This Entity represents sacco charges
 */

@NamedQueries({
    @NamedQuery(name="checkCharge",
                query="SELECT charge1 FROM Charges charge1 WHERE charge1.name =:chargeName"),
     @NamedQuery(name="mandatoryCharges",
                query="SELECT charge1 FROM Charges charge1 WHERE charge1.mandatory =:value")
    
    
})

@Entity
public class Charges implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String name;
    private double cost;
    private String editor;
    private boolean mandatory;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date lastUpdate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
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
        if (!(object instanceof Charges)) {
            return false;
        }
        Charges other = (Charges) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Charges[ id=" + id + " ]";
    }
    
}
