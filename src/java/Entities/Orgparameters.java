

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
@Table(name = "ORGPARAMETERS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Orgparameters.findAll", query = "SELECT o FROM Orgparameters o"),
    @NamedQuery(name = "Orgparameters.findByParameterid", query = "SELECT o FROM Orgparameters o WHERE o.parameterid = :parameterid"),
    @NamedQuery(name = "Orgparameters.findByParametername", query = "SELECT o FROM Orgparameters o WHERE o.parametername = :parametername"),
    @NamedQuery(name = "Orgparameters.findByParametervalue", query = "SELECT o FROM Orgparameters o WHERE o.parametervalue = :parametervalue"),
    @NamedQuery(name = "Orgparameters.findByRequired", query = "SELECT o FROM Orgparameters o WHERE o.required = :required"),
     @NamedQuery(name = "getByOrgani", query = "SELECT o FROM Orgparameters o WHERE o.orgid.orgid = :orgid")

})
public class Orgparameters implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @NotNull
    @Column(name = "PARAMETERID")
    private Long parameterid;
    @Size(max = 255)
    @Column(name = "PARAMETERNAME")
    private String parametername;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "PARAMETERVALUE")
    private String parametervalue;
    @Size(max = 255)
    @Column(name = "REQUIRED")
    private String required;
    @JoinColumn(name = "ORGID", referencedColumnName = "ORGID")
    @ManyToOne(optional = false)
    private Organizations orgid;

    public Orgparameters() {
    }

    public Orgparameters(Long parameterid) {
        this.parameterid = parameterid;
    }

    public Orgparameters(Long parameterid, String parametervalue) {
        this.parameterid = parameterid;
        this.parametervalue = parametervalue;
    }

    public Long getParameterid() {
        return parameterid;
    }

    public void setParameterid(Long parameterid) {
        this.parameterid = parameterid;
    }

    public String getParametername() {
        return parametername;
    }

    public void setParametername(String parametername) {
        this.parametername = parametername;
    }

    public String getParametervalue() {
        return parametervalue;
    }

    public void setParametervalue(String parametervalue) {
        this.parametervalue = parametervalue;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
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
        hash += (parameterid != null ? parameterid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Orgparameters)) {
            return false;
        }
        Orgparameters other = (Orgparameters) object;
        if ((this.parameterid == null && other.parameterid != null) || (this.parameterid != null && !this.parameterid.equals(other.parameterid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Orgparameters[ parameterid=" + parameterid + " ]";
    }
    
}
