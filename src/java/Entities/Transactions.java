/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;

/**
 *
 * @author kabugi & george - PearlSoft Technologies
 */
@NamedQueries({
    @NamedQuery(name="getTransaction",query="SELECT trans FROM Transactions trans WHERE trans.subAccount=:toaccs"),
    @NamedQuery(name="getBalanceDate",query="SELECT SUM(trans.amount) FROM Transactions trans WHERE trans.transactionDate LIKE :transdate AND trans.subAccount =:subAcc"),
    
    @NamedQuery(name="getBalanceList",query="SELECT trans FROM Transactions trans WHERE trans.transactionDate LIKE :transdate AND trans.subAccount =:subAcc"),
    @NamedQuery(name="getBalanceRange",query="SELECT SUM(trans.amount) FROM Transactions trans WHERE trans.subAccount =:subAcc AND trans.transactionDate BETWEEN :fromdate AND :todate  "),
   
    @NamedQuery(name="getDebitRange",query="SELECT SUM(trans.debit) FROM Transactions trans WHERE trans.subAccount =:subAcc AND trans.transactionDate BETWEEN  :fromdate AND :todate"),
    @NamedQuery(name="getCreditRange",query="SELECT SUM(trans.credit) FROM Transactions trans WHERE trans.subAccount =:subAcc AND trans.transactionDate BETWEEN :fromdate AND :todate"),
    
    @NamedQuery(name="getDebit", query = "SELECT SUM(trans.debit) FROM Transactions trans WHERE trans.transactionDate LIKE :transdate AND trans.subAccount =:subAcc"),
    @NamedQuery(name="getCredit", query = "SELECT SUM(trans.credit) FROM Transactions trans WHERE trans.transactionDate LIKE :transdate AND trans.subAccount =:subAcc"),
    
    @NamedQuery(name="getTrans", query="SELECT trans FROM Transactions trans WHERE trans.transId=:transId"),
    
    @NamedQuery(name="searchTrans", query="SELECT trans FROM Transactions trans WHERE trans.contributionID=:contributionID and trans.description =:description and trans.subAccount =:subaccount")
    
    
})
@Entity
public class Transactions implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date transactionDate;
    private String description;
    private String transferAcc;
    private String subAccount;
    private double amount;
    private double debit;
    private double credit;
    private String creator;
    private String accountType;
    private Long transId;
    private boolean writeable =false;
    private Long contributionID;

    public boolean isWriteable() {
        return writeable;
    }

    public void setWriteable(boolean writeable) {
        this.writeable = writeable;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    
    
    public String getSubAccount() {
        return subAccount;
    }

    public void setSubAccount(String subAccount) {
        this.subAccount = subAccount;
    }

    
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTransferAcc() {
        return transferAcc;
    }

    public void setTransferAcc(String transferAcc) {
        this.transferAcc = transferAcc;
    }

   

   

    public double getDebit() {
        return debit;
    }

    public void setDebit(double debit) {
        this.debit = debit;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    
    
  
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
    
    
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTransId() {
        return transId;
    }

    public void setTransId(Long transId) {
        this.transId = transId;
    }

    public Long getContributionID() {
        return contributionID;
    }

    public void setContributionID(Long contributionID) {
        this.contributionID = contributionID;
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
        if (!(object instanceof Transactions)) {
            return false;
        }
        Transactions other = (Transactions) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Transactions[ id=" + id + " ]";
    }

}
