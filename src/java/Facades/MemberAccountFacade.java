

package Facades;

import Entities.MemberAccount;
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
public class MemberAccountFacade extends AbstractFacade<MemberAccount> {
    @PersistenceContext(unitName = "SaccoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MemberAccountFacade() {
        super(MemberAccount.class);
    }
    
    //get member account    
    public MemberAccount getMemberAccount(String memberNumber)
    {     
        MemberAccount memberAccount = new MemberAccount();
        
        Query query = getEntityManager().createNamedQuery("searchAccount");
        
        query.setParameter("membernumber", memberNumber);
        
        if(!query.getResultList().isEmpty())
        {
            memberAccount = (MemberAccount)query.getResultList().get(0);
        }
        else
        {
            memberAccount = null;
        }
        
        return memberAccount;
    }
    
    
     //get member account    
    public MemberAccount findAccountById(String id)
    {     
        MemberAccount memberAccount = new MemberAccount();        
        Query query = getEntityManager().createNamedQuery("searchAccountByID");        
        query.setParameter("accountID", id);        
        if(!query.getResultList().isEmpty())
        {
            memberAccount = (MemberAccount)query.getResultList().get(0);
        }
        else
        {
            memberAccount = null;
        }        
        return memberAccount;
    }
    
    
    
    //filter member deposits
    public List<MemberAccount> filterDeposits(double filterValue,String criteria)
    {  List<MemberAccount> accounts = new ArrayList<MemberAccount>();
        if(criteria.equalsIgnoreCase("depositslessthan")){
        Query query = getEntityManager().createNamedQuery("filterDepositsLess");
        query.setParameter("filtervalue", filterValue);
        
        accounts = query.getResultList();
        }
        else{
       
        Query query = getEntityManager().createNamedQuery("filterDeposits");
        query.setParameter("filtervalue", filterValue);
        
        accounts = query.getResultList();
        
        }
        return accounts;
    }
    
    ///filter member shares
    public List<MemberAccount> filterShares(double filterValue,String criteria)
    {
        List<MemberAccount> accounts = new ArrayList<MemberAccount>();
        if(criteria.equalsIgnoreCase("shareslessthan")){
        Query query = getEntityManager().createNamedQuery("filterSharesLess");
        query.setParameter("filtervalue", filterValue);
        
        accounts = query.getResultList();
        }
        else{
        Query query = getEntityManager().createNamedQuery("filterShares");
        query.setParameter("filtervalue", filterValue);
        
        accounts = query.getResultList();
        }
        return accounts;
    }
    
    

}
