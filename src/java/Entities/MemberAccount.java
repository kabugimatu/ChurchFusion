

package Entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author George / Kabugi Pearl Soft Technologies 
 * All rights reserved
 */
@Entity

@NamedQueries({
        @NamedQuery(name="searchAccount", query="SELECT account FROM MemberAccount account WHERE account.memberNumber =:membernumber"),
        @NamedQuery(name="searchAccountByID", query="SELECT account FROM MemberAccount account WHERE account.id =:accountID"),
        @NamedQuery(name="filterDeposits", query="SELECT account FROM MemberAccount account WHERE account.deposit >=:filtervalue"),
        @NamedQuery(name="filterShares", query="SELECT account FROM MemberAccount account WHERE account.shares >=:filtervalue"),
        @NamedQuery(name="filterSharesLess", query="SELECT account FROM MemberAccount account WHERE account.shares <=:filtervalue"),
        @NamedQuery(name="filterDepositsLess", query="SELECT account FROM MemberAccount account WHERE account.deposit <=:filtervalue")
        
})
public class MemberAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String memberNumber;
    private double shares;
    private double deposit;
    private double dividends;

    public String getMemberNumber() {
        return memberNumber;
    }

    public void setMemberNumber(String memberNumber) {
        this.memberNumber = memberNumber;
    }

    public double getShares() {
        return shares;
    }

    public void setShares(double shares) {
        this.shares = shares;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public double getDividends() {
        return dividends;
    }

    public void setDividends(double dividends) {
        this.dividends = dividends;
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
        if (!(object instanceof MemberAccount)) {
            return false;
        }
        MemberAccount other = (MemberAccount) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.MemberAccount[ id=" + id + " ]";
    }

}
