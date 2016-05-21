

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kabugi
 */
@Entity
@Table(name = "TOWNS")
@XmlRootElement
@NamedQueries({
   
    @NamedQuery(name = "findByTownid", query = "SELECT t FROM Towns t WHERE t.townid = :townid"),
    @NamedQuery(name = "findByTownname", query = "SELECT t FROM Towns t WHERE t.townname = :townname"),
   @NamedQuery(name = "findByTownNameID", query = "SELECT t FROM Towns t WHERE t.townname = :townname AND t.countryid = :countryid")

})
public class Towns implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "TOWNID")
    private Long townid;
    @Size(max = 255)
    @Column(name = "TOWNNAME")
    private String townname;
    @JoinColumn(name = "COUNTRYID", referencedColumnName = "COUNTRYID")
    @ManyToOne(optional = false)
    private Countries countryid;

    public Towns() {
    }

    public Towns(Long townid) {
        this.townid = townid;
    }

    public Long getTownid() {
        return townid;
    }

    public void setTownid(Long townid) {
        this.townid = townid;
    }

    public String getTownname() {
        return townname;
    }

    public void setTownname(String townname) {
        this.townname = townname;
    }

    public Countries getCountryid() {
        return countryid;
    }

    public void setCountryid(Countries countryid) {
        this.countryid = countryid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (townid != null ? townid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Towns)) {
            return false;
        }
        Towns other = (Towns) object;
        if ((this.townid == null && other.townid != null) || (this.townid != null && !this.townid.equals(other.townid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Towns[ townid=" + townid + " ]";
    }
    
}
