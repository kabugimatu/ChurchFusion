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
import javax.persistence.TemporalType;
 
/**
 *
 * @author kabugi
 */
@Entity

@NamedQueries({
    @NamedQuery(name="searchByDate",query = "SELECT disbursement FROM LoanDisbursement disbursement WHERE disbursement.disburseDate LIKE :datedis AND disbursement.bankId=:bid AND disbursement.bankBranchId=:brid"),
    @NamedQuery(name="searchByMonth",query = "SELECT disbursement FROM LoanDisbursement disbursement WHERE disbursement.disburseDate LIKE :month AND disbursement.bankId=:bid AND disbursement.bankBranchId=:brid"),
    @NamedQuery(name="searchByCheque",query = "SELECT disbursement FROM LoanDisbursement disbursement WHERE disbursement.chequeNumber=:cheque"),
    @NamedQuery(name="searchByDateRange",query="SELECT disbursement FROM LoanDisbursement disbursement WHERE disbursement.disburseDate BETWEEN :dateFrom AND :dateTo")
   
    
})

public class LoanDisbursement implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long memberId;
    private String accountNumber;
    private double amount;
    private Long bankId;
    private Long bankBranchId;
    private String chequeNumber;
    private Long loanId;
    private Long disbursedBy;
  
    @Temporal(TemporalType.DATE)
    private Date disburseDate;

    public LoanDisbursement(Long loanKey,Long memberId, String accountNumber, double amount, Long bankId, Long bankBranchId, String cheque, Date disburseDate) {
        this.memberId = memberId;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.bankId = bankId;
        this.bankBranchId = bankBranchId;
        this.chequeNumber = cheque;
        this.disburseDate = disburseDate;
        this.loanId = loanKey;
    }

    public LoanDisbursement() {
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    
    public Long getDisbursedBy() {
        return disbursedBy;
    }

    public void setDisbursedBy(Long disbursedBy) {
        this.disbursedBy = disbursedBy;
    }

    
    
    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public Long getBankBranchId() {
        return bankBranchId;
    }

    public void setBankBranchId(Long bankBranchId) {
        this.bankBranchId = bankBranchId;
    }

    public String getChequeNumber() {
        return chequeNumber;
    }

    public void setChequeNumber(String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    
    
    

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDisburseDate() {
        return disburseDate;
    }

    public void setDisburseDate(Date disburseDate) {
        this.disburseDate = disburseDate;
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
        if (!(object instanceof LoanDisbursement)) {
            return false;
        }
        LoanDisbursement other = (LoanDisbursement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.LoanDisbursement[ id=" + id + " ]";
    }
    
}
