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
 * @author george.gitere
 */
@NamedQueries({
@NamedQuery(name="userLoanApprovals",
            query="select approval from LoanApproval approval where approval.userID =:userID and approval.status =:stage  and approval.type = 'loan'"),
@NamedQuery(name="userTopupApprovals",
            query="select approval from LoanApproval approval where approval.userID =:userID and approval.status =:stage and approval.type = 'topup'"),
@NamedQuery(name="userApproval",
            query="select approval from LoanApproval approval where approval.userID =:userID "
        + "and (approval.status = 'review' or approval.status = 'approved' or approval.status = 'rejected') "
        + "and approval.type = 'loan' and approval.loanID =:loanID and approval.memberNumber =:memberNumber"),
@NamedQuery(name="userTopupApproval",
            query="select approval from LoanApproval approval where approval.userID =:userID and (approval.status = 'review' or approval.status = 'approved' or approval.status = 'rejected') and approval.type = 'topup' and approval.topupID =:topupid"),
@NamedQuery(name="smsApprovers",query="select distinct(approval.userID) from LoanApproval approval where approval.status='review'")
})

//userTopupApproval
@Entity
public class LoanApproval implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long userID;
    private String loanID, topupID;
    private String memberNumber;
    private String status;
    private String comments;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date approvalDate;
    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    

    public String getLoanID() {
        return loanID;
    }

    public void setLoanID(String loanID) {
        this.loanID = loanID;
    }

    public String getTopupID() {
        return topupID;
    }

    public void setTopupID(String topupID) {
        this.topupID = topupID;
    }
        
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMemberNumber() {
        return memberNumber;
    }

    public void setMemberNumber(String memberNumber) {
        this.memberNumber = memberNumber;
    }
            
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
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
        if (!(object instanceof LoanApproval)) {
            return false;
        }
        LoanApproval other = (LoanApproval) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.loanApproval[ id=" + id + " ]";
    }
    
}
