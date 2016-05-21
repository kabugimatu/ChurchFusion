/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SupportBeans;

/**
 *
 * @author kabugi & george - PearlSoft Technologies
 */
public class ContribSupport {
    
    private String month;
    private double deposits;
    private double shares;
    private double interests;
    private double benevolent;
    private double registrationFees;
    private double loanAppFees;

    public double getLoanAppFees() {
        return loanAppFees;
    }

    public void setLoanAppFees(double loanAppFees) {
        this.loanAppFees = loanAppFees;
    }
    
    

    public double getRegistrationFees() {
        return registrationFees;
    }

    public void setRegistrationFees(double registrationFees) {
        this.registrationFees = registrationFees;
    }
    
    
    

    public double getDeposits() {
        return deposits;
    }

    public void setDeposits(double deposits) {
        this.deposits = deposits;
    }

    public double getShares() {
        return shares;
    }

    public void setShares(double shares) {
        this.shares = shares;
    }

    public double getInterests() {
        return interests;
    }

    public void setInterests(double interests) {
        this.interests = interests;
    }

    public double getBenevolent() {
        return benevolent;
    }

    public void setBenevolent(double benevolent) {
        this.benevolent = benevolent;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
    
    
    

}
