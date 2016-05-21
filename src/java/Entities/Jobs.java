

package Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kabugi
 */
@Entity
@Table(name = "JOBS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Jobs.findAll", query = "SELECT j FROM Jobs j"),
    @NamedQuery(name = "Jobs.findByEstablihmPositionid", query = "SELECT j FROM Jobs j WHERE j.establihmPositionid = :establihmPositionid"),
    @NamedQuery(name = "Jobs.findByJobid", query = "SELECT j FROM Jobs j WHERE j.jobid = :jobid"),
    @NamedQuery(name = "Jobs.findById", query = "SELECT j FROM Jobs j WHERE j.id = :id")})
public class Jobs implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ESTABLIHM_POSITIONID")
    private long establihmPositionid;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "JOBID")
    private Long jobid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private long id;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "jobId")
    private List<Establishments> establishmentsList;

    public Jobs() {
    }

    public Jobs(Long jobid) {
        this.jobid = jobid;
    }

    public Jobs(Long jobid, long establihmPositionid, long id) {
        this.jobid = jobid;
        this.establihmPositionid = establihmPositionid;
        this.id = id;
    }

    public long getEstablihmPositionid() {
        return establihmPositionid;
    }

    public void setEstablihmPositionid(long establihmPositionid) {
        this.establihmPositionid = establihmPositionid;
    }

    public Long getJobid() {
        return jobid;
    }

    public void setJobid(Long jobid) {
        this.jobid = jobid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @XmlTransient
    public List<Establishments> getEstablishmentsList() {
        return establishmentsList;
    }

    public void setEstablishmentsList(List<Establishments> establishmentsList) {
        this.establishmentsList = establishmentsList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (jobid != null ? jobid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Jobs)) {
            return false;
        }
        Jobs other = (Jobs) object;
        if ((this.jobid == null && other.jobid != null) || (this.jobid != null && !this.jobid.equals(other.jobid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Jobs[ jobid=" + jobid + " ]";
    }
    
}
