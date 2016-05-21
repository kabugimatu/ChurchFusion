/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Facades;

import Entities.LoanApproval;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author george.gitere
 */
@Stateless
public class LoanApprovalFacade extends AbstractFacade<LoanApproval> {

    @PersistenceContext(unitName = "SaccoPU")
    private EntityManager em;

    // get user logged pending loan approvals
    public List<LoanApproval> userLoanApprovals(Long userID, String stage) {
        
        List<LoanApproval> userApprovals = new ArrayList<LoanApproval>();
        Query query = getEntityManager().createNamedQuery("userLoanApprovals");
        query.setParameter("userID", userID);
        query.setParameter("stage", stage);
        try {
            userApprovals = query.getResultList();
            System.out.println("Gettig count ... " + userApprovals.size());
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        return userApprovals;
    }
    
    public List<Long> smsApprovals()
    {
        Query query = getEntityManager().createNamedQuery("smsApprovers");        
        return query.getResultList();        
    }
    
    // get user logged pending loan approvals
    public List<LoanApproval> userApproval(Long userID, String loanID, String memberNumber) {
        List<LoanApproval> userApprovals = new ArrayList<LoanApproval>();
        Query query = getEntityManager().createNamedQuery("userApproval");
        query.setParameter("userID", userID);
        query.setParameter("loanID", loanID);
        query.setParameter("memberNumber", memberNumber);
        try {
            userApprovals = query.getResultList();
        } 
        catch (Exception e) {
            
        }
        return userApprovals;
    }
    
    
    
      // get user logged pending loan approvals
    public List<LoanApproval> userTopupApproval(Long userID, String topupid) {
        List<LoanApproval> userApprovals = new ArrayList<LoanApproval>();
        Query query = getEntityManager().createNamedQuery("userTopupApproval");
        query.setParameter("userID", userID);
        query.setParameter("topupid", topupid);
        try {
            userApprovals = query.getResultList();
        } 
        catch (Exception e) {
        }
        return userApprovals;
    }
    
     // get user logged pending loan approvals
    public List<LoanApproval> userTopupApprovals(Long userID, String stage) {
        List<LoanApproval> userApprovals = new ArrayList<LoanApproval>();
        Query query = getEntityManager().createNamedQuery("userTopupApprovals");
        query.setParameter("userID", userID);
        query.setParameter("stage", stage);
        try {
            userApprovals = query.getResultList();
        } 
        catch (Exception e) {
        }
        return userApprovals;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LoanApprovalFacade() {
        super(LoanApproval.class);
    }
}
