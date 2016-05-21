/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kabugi
 */
@Entity
@Table(name = "ORGUNITS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Orgunits.findAll", query = "SELECT o FROM Orgunits o"),
    @NamedQuery(name = "Orgunits.findByOrgunitid", query = "SELECT o FROM Orgunits o WHERE o.orgunitid = :orgunitid"),
    @NamedQuery(name = "Orgunits.findByOrgunitname", query = "SELECT o FROM Orgunits o WHERE o.orgunitname = :orgunitname"),
    @NamedQuery(name = "Orgunits.findByStatus", query = "SELECT o FROM Orgunits o WHERE o.status = :status"),
    @NamedQuery(name = "getByOrg", query = "SELECT o FROM Orgunits o WHERE o.orgid.orgid = :orgid"),
    @NamedQuery(name="getByLevel", query="SELECT o FROM Orgunits o WHERE o.hlevel.hlevel = :levelid"),
    @NamedQuery(name="getChildren", query="SELECT o FROM Orgunits o WHERE o.parentOrgunitid.orgunitid =:unitid")
     


})
public class Orgunits implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ORGUNITID")
    private Long orgunitid;
    @Size(max = 255)
    @Column(name = "ORGUNITNAME")
    private String orgunitname;
    @Size(max = 255)
    @Column(name = "STATUS")
    private String status;
    @OneToMany(mappedBy = "parentOrgunitid")
    private List<Orgunits> orgunitsList;
    @JoinColumn(name = "PARENT_ORGUNITID", referencedColumnName = "ORGUNITID")
    @ManyToOne
    private Orgunits parentOrgunitid;
    @JoinColumn(name = "ORGID", referencedColumnName = "ORGID")
    @ManyToOne
    private Organizations orgid;
    @JoinColumn(name = "HLEVEL", referencedColumnName = "HLEVEL")
    @ManyToOne
    private Orglevels hlevel;

    public Orgunits() {
    }

    public Orgunits(Long orgunitid) {
        this.orgunitid = orgunitid;
    }

    public Long getOrgunitid() {
        return orgunitid;
    }

    public void setOrgunitid(Long orgunitid) {
        this.orgunitid = orgunitid;
    }

    public String getOrgunitname() {
        return orgunitname;
    }

    public void setOrgunitname(String orgunitname) {
        this.orgunitname = orgunitname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @XmlTransient
    public List<Orgunits> getOrgunitsList() {
        return orgunitsList;
    }

    public void setOrgunitsList(List<Orgunits> orgunitsList) {
        this.orgunitsList = orgunitsList;
    }

    public Orgunits getParentOrgunitid() {
        return parentOrgunitid;
    }

    public void setParentOrgunitid(Orgunits parentOrgunitid) {
        this.parentOrgunitid = parentOrgunitid;
    }

    public Organizations getOrgid() {
        return orgid;
    }

    public void setOrgid(Organizations orgid) {
        this.orgid = orgid;
    }

    public Orglevels getHlevel() {
        return hlevel;
    }

    public void setHlevel(Orglevels hlevel) {
        this.hlevel = hlevel;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orgunitid != null ? orgunitid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Orgunits)) {
            return false;
        }
        Orgunits other = (Orgunits) object;
        if ((this.orgunitid == null && other.orgunitid != null) || (this.orgunitid != null && !this.orgunitid.equals(other.orgunitid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Orgunits[ orgunitid=" + orgunitid + " ]";
    }
    
}
