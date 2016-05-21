

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
import javax.persistence.TemporalType;

/**
 *
 * @author George / Kabugi Pearl Soft Technologies 
 * All rights reserved
 */

@NamedQueries({
            @NamedQuery(name="searchLoan", query="SELECT loan FROM MemberLoan loan WHERE loan.loanNUmber =:loannumber"),
            @NamedQuery(name="searchMemberLoan", query="SELECT loan FROM MemberLoan loan WHERE loan.loanNUmber =:loannumber and loan.memberNumber =:memberNumber"),
            @NamedQuery(name="searchApprovalLoan", query="SELECT loan FROM MemberLoan loan WHERE loan.loanNUmber =:loannumber"),
            @NamedQuery(name="searchSingleLoan", query="SELECT loan FROM MemberLoan loan WHERE loan.loanNUmber =:loannumber"),
            @NamedQuery(name="filterLoans", query="SELECT loan FROM MemberLoan loan WHERE loan.approvalStatus =:approvalstatus"),
            
            @NamedQuery(name="fineFilterLoans", query="SELECT loan FROM MemberLoan loan WHERE loan.loanNUmber =:loannumber AND loan.approvalStatus =:approvalstatus"),           
            @NamedQuery(name="rejectedLoans", query="SELECT loan FROM MemberLoan loan WHERE loan.status =:status"),            
            @NamedQuery(name="fineFilterRejectedLoans", query="SELECT loan FROM MemberLoan loan WHERE loan.loanNUmber =:loannumber AND loan.status =:status"),
            @NamedQuery(name="unpaidLoans",query="SELECT loan FROM MemberLoan loan WHERE loan.memberNumber =:membernumber AND loan.balance > 0 AND loan.purpose =:ltype"),
            @NamedQuery(name="getUndisbursedLoans", query="SELECT loan FROM MemberLoan loan WHERE loan.disbursementStatus =:disstatus AND loan.status =:appstatus"),
            @NamedQuery(name="getUndisbursedLoans", query="SELECT loan FROM MemberLoan loan WHERE loan.disbursementStatus =:disstatus AND loan.status =:appstatus")
})
@Entity
public class MemberLoan implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String loanNUmber;
    private String positionSociety;
    private String purpose;
    private String witnessNumber;
    private String servedBy;
    private String memberNumber;
    private String status;
    private String approvalReason;
    private boolean approvalStatus;
    private boolean disbursementStatus;
    private String loanTypeId;
    private double appliedAmount,approvedAmount, interest, applicationFee,insuranceFee, amountPayable,balance;
    private double principalPaid, interestPaid, applicationFeePaid,insuranceFeePaid, monthlyInstallment;
    private int repaymentPeriod, defaultedCount;
           
    @Temporal(TemporalType.DATE)
    private Date repayStart ;
    @Temporal(TemporalType.DATE)
    private Date applicationDate ;
    
    @OneToMany
    private List<LoanSecurity> securities = new ArrayList<LoanSecurity>();
    @OneToMany
    private List<LoanGuarantors> guarantors = new ArrayList<LoanGuarantors>();
    @OneToMany
    private List<Users> approvers = new ArrayList<Users>();
    @OneToMany
    private List<LoanApproval> loanApprovals = new ArrayList<LoanApproval>();
    @OneToMany
    private List<LoanTopup> loanTopups = new ArrayList<LoanTopup>();

    public boolean isDisbursementStatus() {
        return disbursementStatus;
    }

    public void setDisbursementStatus(boolean disbursementStatus) {
        this.disbursementStatus = disbursementStatus;
    }
    
    

    public double getInsuranceFee() {
        return insuranceFee;
    }

    public void setInsuranceFee(double insuranceFee) {
        this.insuranceFee = insuranceFee;
    }

    public double getInsuranceFeePaid() {
        return insuranceFeePaid;
    }

    public void setInsuranceFeePaid(double insuranceFeePaid) {
        this.insuranceFeePaid = insuranceFeePaid;
    }

    public double getAppliedAmount() {
        return appliedAmount;
    }

    public void setAppliedAmount(double appliedAmount) {
        this.appliedAmount = appliedAmount;
    }

    public String getLoanTypeId() {
        return loanTypeId;
    }

    public void setLoanTypeId(String loanTypeId) {
        this.loanTypeId = loanTypeId;
    }
        
    public boolean isApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(boolean approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public double getMonthlyInstallment() {
        return monthlyInstallment;
    }

   
    public void setMonthlyInstallment(double monthlyInstallment) {
        this.monthlyInstallment = monthlyInstallment;
    }
    
    public String getMemberNumber() {
        return memberNumber;
    }

    public void setMemberNumber(String memberNumber) {
        this.memberNumber = memberNumber;
    }

    public List<Users> getApprovers() {
        return approvers;
    }

    public void setApprovers(List<Users> approvers) {
        this.approvers = approvers;
    }
      
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoanNUmber() {
        return loanNUmber;
    }

    public double getBalance() {
        return balance;
    }

    
    public void setBalance(double balance) {
        this.balance = balance;
    }

 
    
    public double getPrincipalPaid() {
        return principalPaid;
    }

    public void setPrincipalPaid(double principalPaid) {
        this.principalPaid = principalPaid;
    }

    public double getInterestPaid() {
        return interestPaid;
    }

    public void setInterestPaid(double interestPaid) {
        this.interestPaid = interestPaid;
    }

    public double getApplicationFeePaid() {
        return applicationFeePaid;
    }

    public void setApplicationFeePaid(double applicationFeePaid) {
        this.applicationFeePaid = applicationFeePaid;
    }
    
    

    public void setLoanNUmber(String loanNUmber) {
        this.loanNUmber = loanNUmber;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getApprovalReason() {
        return approvalReason;
    }

    public void setApprovalReason(String approvalReason) {
        this.approvalReason = approvalReason;
    }

        
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
    public String getPositionSociety() {
        return positionSociety;
    }

    public void setPositionSociety(String positionSociety) {
        this.positionSociety = positionSociety;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getWitnessNumber() {
        return witnessNumber;
    }

    public void setWitnessNumber(String witnessNumber) {
        this.witnessNumber = witnessNumber;
    }

    public String getServedBy() {
        return servedBy;
    }

    public void setServedBy(String servedBy) {
        this.servedBy = servedBy;
    }

    
    
    public double getApprovedAmount() {
        return approvedAmount;
    }

    public void setApprovedAmount(double approvedAmount) {
        this.approvedAmount = approvedAmount;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    

    public int getRepaymentPeriod() {
        return repaymentPeriod;
    }

    public void setRepaymentPeriod(int repaymentPeriod) {
        this.repaymentPeriod = repaymentPeriod;
    }

    public int getDefaultedCount() {
        return defaultedCount;
    }

    public void setDefaultedCount(int defaultedCount) {
        this.defaultedCount = defaultedCount;
    }
    
    

    public Date getRepayStart() {
        return repayStart;
    }

    public void setRepayStart(Date repayStart) {
        this.repayStart = repayStart;
    }

    public List<LoanSecurity> getSecurities() {
        return securities;
    }

    public void setSecurities(List<LoanSecurity> securities) {
        this.securities = securities;
    }

    public List<LoanGuarantors> getGuarantors() {
        return guarantors;
    }

    public void setGuarantors(List<LoanGuarantors> guarantors) {
        this.guarantors = guarantors;
    }

    public List<LoanApproval> getLoanApprovals() {
        return loanApprovals;
    }

    public void setLoanApprovals(List<LoanApproval> loanApprovals) {
        this.loanApprovals = loanApprovals;
    }

    public List<LoanTopup> getLoanTopups() {
        return loanTopups;
    }

    public void setLoanTopups(List<LoanTopup> loanTopups) {
        this.loanTopups = loanTopups;
    }
                
    public double getApplicationFee() {
        return applicationFee;
    }

    public void setApplicationFee(double applicationFee) {
        this.applicationFee = applicationFee;
    }

    public double getAmountPayable() {
        return amountPayable;
    }

    public void setAmountPayable(double amountPayable) {
        this.amountPayable = amountPayable;
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
        if (!(object instanceof MemberLoan)) {
            return false;
        }
        MemberLoan other = (MemberLoan) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.MemberLoan[ id=" + id + " ]";
    }

}
