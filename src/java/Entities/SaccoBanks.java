/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author kabugi
 */
@Entity
@NamedQueries({
    @NamedQuery(name="checkBankName",query="SELECT saccobank FROM SaccoBanks saccobank WHERE saccobank.bankName =:bankname")
})
public class SaccoBanks implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String bankName;
    private boolean trash;
    
    
    @OneToMany(cascade = CascadeType.REMOVE)
    private List<BankBranches> branches = new ArrayList<BankBranches>();

    public boolean isTrash() {
        return trash;
    }

    public void setTrash(boolean trash) {
        this.trash = trash;
    }
    
    

    public List<BankBranches> getBranches() {
        return branches;
    }

    public void setBranches(List<BankBranches> branches) {
        this.branches = branches;
    }
    
    

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
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
        if (!(object instanceof SaccoBanks)) {
            return false;
        }
        SaccoBanks other = (SaccoBanks) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.SaccoBanks[ id=" + id + " ]";
    }
    
}
