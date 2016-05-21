
package Entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kabugi
 */
@Entity
@Table(name = "ORGGRADES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Orggrades.findAll", query = "SELECT o FROM Orggrades o"),
    @NamedQuery(name = "Orggrades.findByGradeid", query = "SELECT o FROM Orggrades o WHERE o.gradeid = :gradeid"),
    @NamedQuery(name = "Orggrades.findByGradename", query = "SELECT o FROM Orggrades o WHERE o.gradename = :gradename"),
     @NamedQuery(name = "getByOrgan", query = "SELECT o FROM Orggrades o WHERE o.orgid.orgid = :orgid")

})

public class Orggrades implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @NotNull
    @Column(name = "GRADEID")
    private Long gradeid;
    @Size(max = 255)
    @Column(name = "GRADENAME")
    private String gradename;
    @JoinColumn(name = "ORGID", referencedColumnName = "ORGID")
    @ManyToOne(optional = false)
    private Organizations orgid;

    public Orggrades() {
    }

    public Orggrades(Long gradeid) {
        this.gradeid = gradeid;
    }

    public Long getGradeid() {
        return gradeid;
    }

    public void setGradeid(Long gradeid) {
        this.gradeid = gradeid;
    }

    public String getGradename() {
        return gradename;
    }

    public void setGradename(String gradename) {
        this.gradename = gradename;
    }

    public Organizations getOrgid() {
        return orgid;
    }

    public void setOrgid(Organizations orgid) {
        this.orgid = orgid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (gradeid != null ? gradeid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Orggrades)) {
            return false;
        }
        Orggrades other = (Orggrades) object;
        if ((this.gradeid == null && other.gradeid != null) || (this.gradeid != null && !this.gradeid.equals(other.gradeid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Orggrades[ gradeid=" + gradeid + " ]";
    }
    
}
