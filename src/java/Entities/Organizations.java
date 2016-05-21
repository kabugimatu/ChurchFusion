/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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
@Table(name = "organizations")
@XmlRootElement
@NamedQueries({
 
    @NamedQuery(name = "findByOrgid", query = "SELECT o FROM Organizations o WHERE o.orgid = :orgid"),
    @NamedQuery(name = "findByOrgname", query = "SELECT o FROM Organizations o WHERE o.orgname = :orgname"),
    @NamedQuery(name = "findByOrgtype", query = "SELECT o FROM Organizations o WHERE o.orgtype = :orgtype"),
    @NamedQuery(name = "findByPostaladdress", query = "SELECT o FROM Organizations o WHERE o.postaladdress = :postaladdress"),
    @NamedQuery(name = "findByStreet", query = "SELECT o FROM Organizations o WHERE o.street = :street"),
    @NamedQuery(name = "findByBuilding", query = "SELECT o FROM Organizations o WHERE o.building = :building"),
    @NamedQuery(name = "findByEmail", query = "SELECT o FROM Organizations o WHERE o.email = :email"),
    @NamedQuery(name = "findByTelephone", query = "SELECT o FROM Organizations o WHERE o.telephone = :telephone"),
    @NamedQuery(name = "findByTelephone2", query = "SELECT o FROM Organizations o WHERE o.telephone2 = :telephone2"),
    @NamedQuery(name = "findByWebsite", query = "SELECT o FROM Organizations o WHERE o.website = :website"),
    @NamedQuery(name = "findChildren", query = "SELECT o FROM Organizations o WHERE o.parentOrgid.orgid = :parentid")

})
public class Organizations implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ORGID")
    private Long orgid;
    @Size(max = 255)
    @Column(name = "BUILDING")
    private String building;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "EMAIL")
    private String email;
    @Lob
    @Column(name = "LOGO")
    private byte[] logo;
    @Size(max = 255)
    @Column(name = "ORGNAME")
    private String orgname;
    @Size(max = 255)
    @Column(name = "ORGTYPE")
    private String orgtype;
    @Size(max = 255)
    @Column(name = "POSTALADDRESS")
    private String postaladdress;
    @Size(max = 255)
    @Column(name = "STREET")
    private String street;
    @Size(max = 255)
    @Column(name = "TELEPHONE")
    private String telephone;
    @Size(max = 255)
    @Column(name = "TELEPHONE2")
    private String telephone2;
    @Size(max = 255)
    @Column(name = "WEBSITE")
    private String website;
   
    @OneToMany(mappedBy = "parentOrgid")
    private List<Organizations> organizationsList;
    @JoinColumn(name = "PARENT_ORGID", referencedColumnName = "ORGID")
    @ManyToOne
    private Organizations parentOrgid;
    
   @JoinColumn(name = "COUNTRYID", referencedColumnName = "COUNTRYID")
    @ManyToOne
    private Countries countryId;
    
  
    @OneToMany(mappedBy = "orgid")
    private List<Orgunits> orgunitsList;
 @OneToMany(cascade = CascadeType.ALL, mappedBy = "orgid")
    private List<Orggrades> orggradesList;
      @OneToMany(cascade = CascadeType.ALL, mappedBy = "orgid")
    private List<Establishments> establishmentsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orgid")
    private List<Orgparameters> orgparametersList;
 
    public Organizations() {
    }

    public Organizations(Long orgid) {
        this.orgid = orgid;
    }

    public Long getOrgid() {
        return orgid;
    }

    public void setOrgid(Long orgid) {
        this.orgid = orgid;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

     public Countries getCountryId() {
        return countryId;
    }

    public void setCountryId(Countries countryId) {
        this.countryId = countryId;
    }

    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    public String getOrgtype() {
        return orgtype;
    }

    public void setOrgtype(String orgtype) {
        this.orgtype = orgtype;
    }

    public String getPostaladdress() {
        return postaladdress;
    }

    public void setPostaladdress(String postaladdress) {
        this.postaladdress = postaladdress;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getTelephone2() {
        return telephone2;
    }

    public void setTelephone2(String telephone2) {
        this.telephone2 = telephone2;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

   
    @XmlTransient
    public List<Organizations> getOrganizationsList() {
        return organizationsList;
    }

    public void setOrganizationsList(List<Organizations> organizationsList) {
        this.organizationsList = organizationsList;
    }

   
    
    public Organizations getParentOrgid() {
        return parentOrgid;
    }
 @XmlTransient
    public List<Orggrades> getOrggradesList() {
        return orggradesList;
    }

    public void setOrggradesList(List<Orggrades> orggradesList) {
        this.orggradesList = orggradesList;
    }
    

    public void setParentOrgid(Organizations parentOrgid) {
        this.parentOrgid = parentOrgid;
    }
 @XmlTransient
   public List<Establishments> getEstablishmentsList() {
        return establishmentsList;
    }

    public void setEstablishmentsList(List<Establishments> establishmentsList) {
        this.establishmentsList = establishmentsList;
    }
 @XmlTransient
    public List<Orgparameters> getOrgparametersList() {
        return orgparametersList;
    }

    public void setOrgparametersList(List<Orgparameters> orgparametersList) {
        this.orgparametersList = orgparametersList;
    }

  

  

    @XmlTransient
    public List<Orgunits> getOrgunitsList() {
        return orgunitsList;
    }

    public void setOrgunitsList(List<Orgunits> orgunitsList) {
        this.orgunitsList = orgunitsList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orgid != null ? orgid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Organizations)) {
            return false;
        }
        Organizations other = (Organizations) object;
        if ((this.orgid == null && other.orgid != null) || (this.orgid != null && !this.orgid.equals(other.orgid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Organizations[ orgid=" + orgid + " ]";
    }
    
}
