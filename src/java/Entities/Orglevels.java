
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
 * @author kabugi
 */
@Entity

@NamedQueries({
    @NamedQuery(name = "findByLevelID", query = "SELECT o FROM Orglevels o WHERE o.hlevel = :hlevel"),
    @NamedQuery(name = "findByName", query = "SELECT o FROM Orglevels o WHERE o.levelname = :levelName"),
   
    
})
public class Orglevels implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
     @GeneratedValue(strategy = GenerationType.AUTO)
    private Long levelid;
   
    private long hlevel;
     
    private String levelname;
   
    
    private boolean applyLoan;
    private boolean loadSettings;
    private boolean addMember;
    private boolean loadMember;
    private boolean deleteMember;
    private boolean approveLoans;
    private boolean makeContribution;
    private boolean loadFinacialStatements;
    private boolean loadReports;
    private boolean loadLedgerAccounts;
    private boolean manageUsers;
    private boolean loadMemberStatement;
    private boolean loadLoanStatement;
    private boolean loadLoans;
    private boolean transferSharers;
    private boolean disburseLoans;
    private boolean manageTransactions;
    private boolean lastApprover;
    
    public Orglevels() {
    }

    public boolean isDisburseLoans() {
        return disburseLoans;
    }

    public void setDisburseLoans(boolean disburseLoans) {
        this.disburseLoans = disburseLoans;
    }

    public boolean isManageTransactions() {
        return manageTransactions;
    }

    public void setManageTransactions(boolean manageTransactions) {
        this.manageTransactions = manageTransactions;
    }
          
    public boolean isTransferSharers() {
        return transferSharers;
    }

    public void setTransferSharers(boolean transferSharers) {
        this.transferSharers = transferSharers;
    }
    
    public boolean isLoadMemberStatement() {
        return loadMemberStatement;
    }

    public void setLoadMemberStatement(boolean loadMemberStatement) {
        this.loadMemberStatement = loadMemberStatement;
    }

    public boolean isLoadLoanStatement() {
        return loadLoanStatement;
    }

    public void setLoadLoanStatement(boolean loadLoanStatement) {
        this.loadLoanStatement = loadLoanStatement;
    }

    public boolean isLoadLoans() {
        return loadLoans;
    }

    public void setLoadLoans(boolean loadLoans) {
        this.loadLoans = loadLoans;
    }

    
    
    public boolean isManageUsers() {
        return manageUsers;
    }

    public void setManageUsers(boolean manageUsers) {
        this.manageUsers = manageUsers;
    }

    
    public boolean isLoadFinacialStatements() {
        return loadFinacialStatements;
    }

    public void setLoadFinacialStatements(boolean loadFinacialStatements) {
        this.loadFinacialStatements = loadFinacialStatements;
    }

    public boolean isLoadReports() {
        return loadReports;
    }

    public void setLoadReports(boolean loadReports) {
        this.loadReports = loadReports;
    }

    public boolean isLoadLedgerAccounts() {
        return loadLedgerAccounts;
    }

    public void setLoadLedgerAccounts(boolean loadLedgerAccounts) {
        this.loadLedgerAccounts = loadLedgerAccounts;
    }

   
    
    public boolean isApplyLoan() {
        return applyLoan;
    }

    public void setApplyLoan(boolean applyLoan) {
        this.applyLoan = applyLoan;
    }

    public boolean isLoadSettings() {
        return loadSettings;
    }

    public void setLoadSettings(boolean loadSettings) {
        this.loadSettings = loadSettings;
    }

    public boolean isAddMember() {
        return addMember;
    }

    public void setAddMember(boolean addMember) {
        this.addMember = addMember;
    }

    public boolean isLoadMember() {
        return loadMember;
    }

    public void setLoadMember(boolean loadMember) {
        this.loadMember = loadMember;
    }

    public boolean isDeleteMember() {
        return deleteMember;
    }

    public void setDeleteMember(boolean deleteMember) {
        this.deleteMember = deleteMember;
    }

    public boolean isApproveLoans() {
        return approveLoans;
    }

    public void setApproveLoans(boolean approveLoans) {
        this.approveLoans = approveLoans;
    }

    public boolean isMakeContribution() {
        return makeContribution;
    }

    public void setMakeContribution(boolean makeContribution) {
        this.makeContribution = makeContribution;
    }

    public boolean isLastApprover() {
        return lastApprover;
    }

    public void setLastApprover(boolean lastApprover) {
        this.lastApprover = lastApprover;
    }
    
           
    public Orglevels(Long levelid) {
        this.levelid = levelid;
    }

    public Orglevels(Long levelid, long hlevel) {
        this.levelid = levelid;
        this.hlevel = hlevel;
    }

   
    

    public Long getLevelid() {
        return levelid;
    }

    public void setLevelid(Long levelid) {
        this.levelid = levelid;
    }

    public long getHlevel() {
        return hlevel;
    }

    public void setHlevel(long hlevel) {
        this.hlevel = hlevel;
    }

    public String getLevelname() {
        return levelname;
    }

    public void setLevelname(String levelname) {
        this.levelname = levelname;
    }

   

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (levelid != null ? levelid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Orglevels)) {
            return false;
        }
        Orglevels other = (Orglevels) object;
        if ((this.levelid == null && other.levelid != null) || (this.levelid != null && !this.levelid.equals(other.levelid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Orglevels[ levelid=" + levelid + " ]";
    }
    
}
