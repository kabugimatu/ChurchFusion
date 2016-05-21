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
 * @author George / Kabugi Pearl Soft Technologies All rights reserved
 */
@NamedQueries({
    @NamedQuery(name = "searchContribution",
            query = "SELECT contribution1 FROM Contribution contribution1 WHERE contribution1.memberNumber =:membernumber AND contribution1.contributionDate LIKE :querydate and contribution1.status =true"),
    @NamedQuery(name = "loanContribution",
            query = "SELECT contribution1 FROM Contribution contribution1 WHERE contribution1.memberNumber =:membernumber AND contribution1.loanNumber =:loannumber and contribution1.status =true"),
    @NamedQuery(name = "monthlyContributions",
            query = "SELECT contribution1 FROM Contribution contribution1 WHERE contribution1.contributionDate LIKE :month and contribution1.status =true"),
    @NamedQuery(name = "checkInterest",
            query = "SELECT contribution1 FROM Contribution contribution1 WHERE contribution1.loanNumber =:loannumber AND contribution1.contributionDate LIKE :querydate and contribution1.status =true"),
    @NamedQuery(name = "sumShares", query = "SELECT SUM(contribution1.shares) FROM Contribution contribution1 WHERE contribution1.contributionDate LIKE :month and contribution1.status =true"),
    @NamedQuery(name = "ccontributionByDateRange",
            query = "SELECT dbContribution FROM Contribution dbContribution WHERE (dbContribution.contributionDate BETWEEN :fromDate and :toDate) and dbContribution.memberNumber =:memberNumber and dbContribution.status =true"),
     @NamedQuery(name = "allcontributionByDateRange",
            query = "SELECT dbContribution FROM Contribution dbContribution WHERE (dbContribution.contributionDate BETWEEN :fromDate and :toDate) and dbContribution.status =true"),
    @NamedQuery(name = "userContributionByDateRange",
            query = "SELECT dbContribution FROM Contribution dbContribution WHERE (dbContribution.contributionDate BETWEEN :fromDate and :toDate) and dbContribution.servedBy =:userName and dbContribution.status =true"),
    @NamedQuery(name = "usersContributionByDateRange",
            query = "SELECT dbContribution FROM Contribution dbContribution WHERE (dbContribution.contributionDate BETWEEN :fromDate and :toDate) and dbContribution.status =true"),
    @NamedQuery(name = "sumDeposits", query = "SELECT SUM(contribution1.deposit) FROM Contribution contribution1 WHERE contribution1.contributionDate LIKE :month and contribution1.status =true"),
    @NamedQuery(name = "sumInterests", query = "SELECT SUM(contribution1.loanInterest) FROM Contribution contribution1 WHERE contribution1.contributionDate LIKE :month and contribution1.status =true"),
    @NamedQuery(name = "sumBenevolent", query = "SELECT SUM(contribution1.benevolent) FROM Contribution contribution1 WHERE contribution1.contributionDate LIKE :month and contribution1.status =true"),
    @NamedQuery(name = "sumRegFee", query = "SELECT SUM(contribution1.registrationFees) FROM Contribution contribution1 WHERE contribution1.contributionDate LIKE :month and contribution1.status =true"),
    @NamedQuery(name = "sumAppFee", query = "SELECT SUM(contribution1.applicationFee) FROM Contribution contribution1 WHERE contribution1.contributionDate LIKE :month and contribution1.status =true")
})
@Entity
public class Contribution implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double total;
    private double shares;
    private double deposit;
    private double loanPrincipal;
    private double loanInterest;
    private double applicationFee;
    private double benevolent;
    private double registrationFees;
    private double insuranceFee;
    private String loanNumber;
    private String servedBy;
    private String memberNumber;
    private double previousBalance;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date contributionDate;
    private String editReason;
    private boolean status = true;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getInsuranceFee() {
        return insuranceFee;
    }

    public void setInsuranceFee(double insuranceFee) {
        this.insuranceFee = insuranceFee;
    }

    public double getPreviousBalance() {
        return previousBalance;
    }

    public void setPreviousBalance(double previousBalance) {
        this.previousBalance = previousBalance;
    }

    public double getRegistrationFees() {
        return registrationFees;
    }

    public void setRegistrationFees(double registrationFees) {
        this.registrationFees = registrationFees;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
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

    public double getLoanPrincipal() {
        return loanPrincipal;
    }

    public void setLoanPrincipal(double loanPrincipal) {
        this.loanPrincipal = loanPrincipal;
    }

    public double getLoanInterest() {
        return loanInterest;
    }

    public void setLoanInterest(double loanInterest) {
        this.loanInterest = loanInterest;
    }

    public double getApplicationFee() {
        return applicationFee;
    }

    public void setApplicationFee(double applicationFee) {
        this.applicationFee = applicationFee;
    }

    public double getBenevolent() {
        return benevolent;
    }

    public void setBenevolent(double benevolent) {
        this.benevolent = benevolent;
    }

    public String getServedBy() {
        return servedBy;
    }

    public void setServedBy(String servedBy) {
        this.servedBy = servedBy;
    }

    public String getMemberNumber() {
        return memberNumber;
    }

    public void setMemberNumber(String memberNumber) {
        this.memberNumber = memberNumber;
    }

    public String getLoanNumber() {
        return loanNumber;
    }

    public void setLoanNumber(String loanNumber) {
        this.loanNumber = loanNumber;
    }

    public Date getContributionDate() {
        return contributionDate;
    }

    public void setContributionDate(Date contributionDate) {
        this.contributionDate = contributionDate;
    }

    public String getEditReason() {
        return editReason;
    }

    public void setEditReason(String editReason) {
        this.editReason = editReason;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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
        if (!(object instanceof Contribution)) {
            return false;
        }
        Contribution other = (Contribution) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Contribution[ id=" + id + " ]";
    }
}
