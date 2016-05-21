

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kabugi
 */
@Entity
@Table(name = "COUNTRIES")
@XmlRootElement
@NamedQueries({
   
    @NamedQuery(name = "findByCountryid", query = "SELECT c FROM Countries c WHERE c.countryid = :countryid"),
    @NamedQuery(name = "findByCountry", query = "SELECT c FROM Countries c WHERE c.country = :country"),
    @NamedQuery(name = "findByDialingcode", query = "SELECT c FROM Countries c WHERE c.dialingcode = :dialingcode"),
    @NamedQuery(name = "findByResidency", query = "SELECT c FROM Countries c WHERE c.residency = :residency")})
public class Countries implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "COUNTRYID")
    private Long countryid;
    @Size(max = 255)
    @Column(name = "COUNTRY")
    private String country;
    @Size(max = 255)
    @Column(name = "DIALINGCODE")
    private String dialingcode;
    @Size(max = 255)
    @Column(name = "RESIDENCY")
    private String residency;
    @OneToMany(mappedBy = "countryId")
    private List<Organizations> organizationsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "countryid")
    private List<Towns> townsList;

    public Countries() {
    }

    public Countries(Long countryid) {
        this.countryid = countryid;
    }

    public Long getCountryid() {
        return countryid;
    }

    public void setCountryid(Long countryid) {
        this.countryid = countryid;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDialingcode() {
        return dialingcode;
    }

    public void setDialingcode(String dialingcode) {
        this.dialingcode = dialingcode;
    }

    public String getResidency() {
        return residency;
    }

    public void setResidency(String residency) {
        this.residency = residency;
    }

    @XmlTransient
    public List<Organizations> getOrganizationsList() {
        return organizationsList;
    }

    public void setOrganizationsList(List<Organizations> organizationsList) {
        this.organizationsList = organizationsList;
    }

    @XmlTransient
    public List<Towns> getTownsList() {
        return townsList;
    }

    public void setTownsList(List<Towns> townsList) {
        this.townsList = townsList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (countryid != null ? countryid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Countries)) {
            return false;
        }
        Countries other = (Countries) object;
        if ((this.countryid == null && other.countryid != null) || (this.countryid != null && !this.countryid.equals(other.countryid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Countries[ countryid=" + countryid + " ]";
    }
    
}
