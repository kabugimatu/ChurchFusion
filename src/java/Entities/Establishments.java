

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
@Table(name = "ESTABLISHMENTS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Establishments.findAll", query = "SELECT e FROM Establishments e"),
    @NamedQuery(name = "Establishments.findByPositionid", query = "SELECT e FROM Establishments e WHERE e.positionid = :positionid"),
    @NamedQuery(name = "Establishments.findByPosition", query = "SELECT e FROM Establishments e WHERE e.position = :position"),
    @NamedQuery(name = "Establishments.findBySeniority", query = "SELECT e FROM Establishments e WHERE e.seniority = :seniority"),
    @NamedQuery(name = "Establishments.findByReportsTo", query = "SELECT e FROM Establishments e WHERE e.reportsTo = :reportsTo")})
public class Establishments implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "POSITIONID")
    private Long positionid;
    @Size(max = 255)
    @Column(name = "POSITION")
    private String position;
    @Size(max = 255)
    @Column(name = "SENIORITY")
    private String seniority;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "REPORTS_TO")
    private String reportsTo;
    @JoinColumn(name = "JOB_ID", referencedColumnName = "JOBID")
    @ManyToOne(optional = false)
    private Jobs jobId;
    @JoinColumn(name = "ORGID", referencedColumnName = "ORGID")
    @ManyToOne(optional = false)
    private Organizations orgid;

    public Establishments() {
    }

    public Establishments(Long positionid) {
        this.positionid = positionid;
    }

    public Establishments(Long positionid, String reportsTo) {
        this.positionid = positionid;
        this.reportsTo = reportsTo;
    }

    public Long getPositionid() {
        return positionid;
    }

    public void setPositionid(Long positionid) {
        this.positionid = positionid;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSeniority() {
        return seniority;
    }

    public void setSeniority(String seniority) {
        this.seniority = seniority;
    }

    public String getReportsTo() {
        return reportsTo;
    }

    public void setReportsTo(String reportsTo) {
        this.reportsTo = reportsTo;
    }

    public Jobs getJobId() {
        return jobId;
    }

    public void setJobId(Jobs jobId) {
        this.jobId = jobId;
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
        hash += (positionid != null ? positionid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Establishments)) {
            return false;
        }
        Establishments other = (Establishments) object;
        if ((this.positionid == null && other.positionid != null) || (this.positionid != null && !this.positionid.equals(other.positionid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Establishments[ positionid=" + positionid + " ]";
    }
    
}
