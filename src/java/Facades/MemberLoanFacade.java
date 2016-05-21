

package Facades;

import Entities.MemberLoan;
import Entities.SaccoMember;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author George / Kabugi Pearl Soft Technologies 
 * All rights reserved
 */
@Stateless
public class MemberLoanFacade extends AbstractFacade<MemberLoan> {
    @PersistenceContext(unitName = "SaccoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MemberLoanFacade() {
        super(MemberLoan.class);
    }
    
     public  List<MemberLoan> getLoanByDisbursement(String appstatus, boolean lStatus)
    {
        List<MemberLoan> allDbLoans = new  ArrayList<MemberLoan>();
        
        Query query = getEntityManager().createNamedQuery("getUndisbursedLoans");
        query.setParameter("disstatus", lStatus);
         query.setParameter("appstatus", appstatus);
        try{
            allDbLoans = query.getResultList();
        }
        catch(Exception ex){
            allDbLoans = new ArrayList<MemberLoan>();
        }
        
        return allDbLoans;
    }
    
    //Get unpaid loans
    public List<MemberLoan> getUnpaidLoans(SaccoMember sMember,String loanType){
        List<MemberLoan> allLoans = new ArrayList<MemberLoan>();
        Query loanQuery = getEntityManager().createNamedQuery("unpaidLoans");
        loanQuery.setParameter("membernumber", sMember.getMemberNumber());
        loanQuery.setParameter("ltype", loanType);
        allLoans = loanQuery.getResultList();
        
        return allLoans;
    }
    
    //generate loan number
    public int generateLoanNumber()
    {
      
        List<MemberLoan> allLoans = new ArrayList<MemberLoan>();
        int loansCount = 0;
        allLoans = findAll();
        if(allLoans.isEmpty())
        {
            loansCount =1;
        }
        else
        {
            loansCount = allLoans.size() + 1;
        }
       
        return loansCount;
        
    }
    
    //find loan by loan number
    public MemberLoan findLoan(String loanNumber)
    {
        MemberLoan loan = new MemberLoan();
        Query query = getEntityManager().createNamedQuery("searchLoan");
        query.setParameter("loannumber", loanNumber);         
        if(query.getResultList().size()>0)
        {
          loan = (MemberLoan)query.getResultList().get(0);
        }        
        return loan;
    }
    
     //find loan by loan number
    public MemberLoan findMemberLoan(String loanNumber, String memberNumber)
    {
        System.out.println("getting loan number ... " + loanNumber + "  member number ... " + memberNumber);
        
        MemberLoan loan = new MemberLoan();
        Query query = getEntityManager().createNamedQuery("searchMemberLoan");
        query.setParameter("loannumber", loanNumber); 
        query.setParameter("memberNumber", memberNumber); 
        if(query.getResultList().size()>0)
        {
          loan = (MemberLoan)query.getResultList().get(0);
        }        
        return loan;
    }
    
     //find loan for approval by loan number
    /*
    public MemberLoan findApprovalLoan(String loanNumber)
    {       
        MemberLoan loan = new MemberLoan();
        Query query = getEntityManager().createNamedQuery("searchApprovalLoan");
        query.setParameter("loannumber", loanNumber);        
        if(query.getResultList().size()>0)
        {
          loan = (MemberLoan)query.getResultList().get(0);
        }        
        return loan;
    }
    */
    public List<MemberLoan> findApprovalLoan(String loanNumber)
    {       
        MemberLoan loan = new MemberLoan();
        Query query = getEntityManager().createNamedQuery("searchApprovalLoan");
        query.setParameter("loannumber", loanNumber);                     
        return query.getResultList();
    }
    
    //filterLoans new loans
    public List<MemberLoan> filterLoans(boolean approvalStatus)
    {
       Query query = getEntityManager().createNamedQuery("filterLoans");
       query.setParameter("approvalstatus", approvalStatus);
       
       return query.getResultList();
    }
    
    //rejected loans
    public List<MemberLoan> rejectedLoans(String Status)
    {
       Query query = getEntityManager().createNamedQuery("rejectedLoans");
       query.setParameter("status", Status);
       
       return query.getResultList();
    }
    
    //rejected loans
    public List<MemberLoan> fineFilterRejectedLoans(String Status, String loannumber)
    {
       Query query = getEntityManager().createNamedQuery("fineFilterRejectedLoans");
       query.setParameter("loannumber", loannumber);
       query.setParameter("status", Status);
       
       return query.getResultList();
    }
    
    // search individual loan
    public List<MemberLoan> searchSingleLoan(String loanNumber)
    {
        Query query = getEntityManager().createNamedQuery("searchSingleLoan");
        query.setParameter("loannumber", loanNumber);       
        return query.getResultList();
    }
    
     // fine search loans
    public List<MemberLoan> fineFilterLoan(String loanNumber, boolean approvalStatus)
    {
        Query query = getEntityManager().createNamedQuery("fineFilterLoans");
        query.setParameter("loannumber", loanNumber); 
        query.setParameter("approvalstatus", approvalStatus); 
        return query.getResultList();
    }
    
    
}
