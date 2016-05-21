/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author george.gitere
 */
@NamedQueries({
@NamedQuery(name = "findTopup",
            query = "select topup from LoanTopup topup where topup.loanNumber =:loanNumber")
})
@Entity
public class LoanTopup implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String loanNumber, purpose, witness;
    private String memberNumber, approvalReason;
    private double appliedAmount, interest, insuranceFee;
    private int repaymentPeriod;
    private String userName;
    private String status;
    private double grandPrincipal, grandInterest, amountPayable;
    private boolean approvalStatus;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateApplied;
    @OneToMany
    private List<Users> topupApprovers = new ArrayList<Users>(); 
    @OneToMany
    private List<LoanGuarantors> topupGuarantors = new ArrayList<LoanGuarantors>();
    @OneToMany 
    private List<LoanSecurity> topupSecurity = new ArrayList<LoanSecurity>();
     @OneToMany
    private List<LoanApproval> topupApprovals = new ArrayList<LoanApproval>();

    public String getApprovalReason() {
        return approvalReason;
    }

    public void setApprovalReason(String approvalReason) {
        this.approvalReason = approvalReason;
    }
     
          
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoanNumber() {
        return loanNumber;
    }

    public void setLoanNumber(String loanNumber) {
        this.loanNumber = loanNumber;
    }

    public int getRepaymentPeriod() {
        return repaymentPeriod;
    }

    public void setRepaymentPeriod(int repaymentPeriod) {
        this.repaymentPeriod = repaymentPeriod;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getWitness() {
        return witness;
    }

    public void setWitness(String witness) {
        this.witness = witness;
    }
           
    public String getMemberNumber() {
        return memberNumber;
    }

    public void setMemberNumber(String memberNumber) {
        this.memberNumber = memberNumber;
    }

    public double getAppliedAmount() {
        return appliedAmount;
    }

    public void setAppliedAmount(double appliedAmount) {
        this.appliedAmount = appliedAmount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getInsuranceFee() {
        return insuranceFee;
    }

    public void setInsuranceFee(double insuranceFee) {
        this.insuranceFee = insuranceFee;
    }

    public double getAmountPayable() {
        return amountPayable;
    }

    public void setAmountPayable(double amountPayable) {
        this.amountPayable = amountPayable;
    }

    public boolean isApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(boolean approvalStatus) {
        this.approvalStatus = approvalStatus;
    }
        
    public Date getDateApplied() {
        return dateApplied;
    }

    public void setDateApplied(Date dateApplied) {
        this.dateApplied = dateApplied;
    }

    public double getGrandPrincipal() {
        return grandPrincipal;
    }

    public void setGrandPrincipal(double grandPrincipal) {
        this.grandPrincipal = grandPrincipal;
    }

    public double getGrandInterest() {
        return grandInterest;
    }

    public void setGrandInterest(double grandInterest) {
        this.grandInterest = grandInterest;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public List<Users> getTopupApprovers() {
        return topupApprovers;
    }

    public void setTopupApprovers(List<Users> topupApprovers) {
        this.topupApprovers = topupApprovers;
    }

    public List<LoanGuarantors> getTopupGuarantors() {
        return topupGuarantors;
    }

    public void setTopupGuarantors(List<LoanGuarantors> topupGuarantors) {
        this.topupGuarantors = topupGuarantors;
    }

    public List<LoanSecurity> getTopupSecurity() {
        return topupSecurity;
    }

    public void setTopupSecurity(List<LoanSecurity> topupSecurity) {
        this.topupSecurity = topupSecurity;
    }

    public List<LoanApproval> getTopupApprovals() {
        return topupApprovals;
    }

    public void setTopupApprovals(List<LoanApproval> topupApprovals) {
        this.topupApprovals = topupApprovals;
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
        if (!(object instanceof LoanTopup)) {
            return false;
        }
        LoanTopup other = (LoanTopup) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.LoanTopup[ id=" + id + " ]";
    }
    
}
