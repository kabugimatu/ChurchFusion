
package Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author george / kabugi
 * 
 */
@Entity

@NamedQueries({
    @NamedQuery(name="checkType",query="SELECT typeloan FROM LoanSettings typeloan WHERE typeloan.loanType=:typename")
    
})

public class LoanSettings implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private int maxRepaymentPeriod=0;
     private int minMembershipPeriod;
     private int maxLoansCount;
    private double loanTimes;
    private int maxGuarantees;
    private double applicationFee=0.0;
    private double insuranceFee = 0.0;
    private double interestRate=0.0;
    private double loanLimit =0.0;
    private double loanSalaryRatio = 0.0;
    private String editor;
    private String loanType;
    private String description;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date lastUpdate;
    private boolean securityCoverLoan=true;
    @OneToMany
    private List<LevelApprovers> orgLevels = new ArrayList<LevelApprovers>();
    @OneToMany
    private List<Users> approvers = new ArrayList<Users>();
    

    public double getInsuranceFee() {
        return insuranceFee;
    }

    public void setInsuranceFee(double insuranceFee) {
        this.insuranceFee = insuranceFee;
    }

    public double getLoanSalaryRatio() {
        return loanSalaryRatio;
    }

    public void setLoanSalaryRatio(double loanSalaryRatio) {
        this.loanSalaryRatio = loanSalaryRatio;
    }

    public List<LevelApprovers> getOrgLevels() {
        return orgLevels;
    }

    public void setOrgLevels(List<LevelApprovers> orgLevels) {
        this.orgLevels = orgLevels;
    }

    
            
    public int getMaxRepaymentPeriod() {
        return maxRepaymentPeriod;
    }

    public int getMinMembershipPeriod() {
        return minMembershipPeriod;
    }

    public void setMinMembershipPeriod(int minMembershipPeriod) {
        this.minMembershipPeriod = minMembershipPeriod;
    }

    public int getMaxLoansCount() {
        return maxLoansCount;
    }

    public void setMaxLoansCount(int maxLoansCount) {
        this.maxLoansCount = maxLoansCount;
    }

    public double getLoanTimes() {
        return loanTimes;
    }

    public void setLoanTimes(double loanTimes) {
        this.loanTimes = loanTimes;
    }

    public List<Users> getApprovers() {
        return approvers;
    }

    public void setApprovers(List<Users> approvers) {
        this.approvers = approvers;
    }
   

    public int getMaxGuarantees() {
        return maxGuarantees;
    }

    public void setMaxGuarantees(int maxGuarantees) {
        this.maxGuarantees = maxGuarantees;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
    
    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    
    public boolean isSecurityCoverLoan() {
        return securityCoverLoan;
    }

    public void setSecurityCoverLoan(boolean securityCoverLoan) {
        this.securityCoverLoan = securityCoverLoan;
    }

    
    
    public void setMaxRepaymentPeriod(int maxRepaymentPeriod) {
        this.maxRepaymentPeriod = maxRepaymentPeriod;
    }

    public double getApplicationFee() {
        return applicationFee;
    }

    public void setApplicationFee(double applicationFee) {
        this.applicationFee = applicationFee;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public double getLoanLimit() {
        return loanLimit;
    }

    public void setLoanLimit(double loanLimit) {
        this.loanLimit = loanLimit;
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
        if (!(object instanceof LoanSettings)) {
            return false;
        }
        LoanSettings other = (LoanSettings) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.LoanSettings[ id=" + id + " ]";
    }
    
}
