/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Facades;

import Entities.Accounts;
import Entities.SubAccounts;
import Entities.Transactions;
import SupportBeans.TransactionsSupport;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author kabugi & george - PearlSoft Technologies
 */
@Stateless
public class TransactionsFacade extends AbstractFacade<Transactions> {
    @PersistenceContext(unitName = "SaccoPU")
    private EntityManager em;
    private SimpleDateFormat df;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
//     public List<Transactions> getSavingsAccTransactions(String toAcc){
//        List<Transactions> allTransactions = new ArrayList<Transactions>();
//        Query query = getEntityManager().createNamedQuery("getSavingsTransaction");
//        query.setParameter("toaccs", toAcc);
//        
//        allTransactions = query.getResultList();
//        
//        return allTransactions;
//    }
    
     public List<Transactions> searchTransaction(Long contributionID, String description, String subaccount){
        List<Transactions> dbTransactions = new ArrayList<Transactions>();
        Query query = getEntityManager().createNamedQuery("searchTrans");
        query.setParameter("contributionID", contributionID);
        query.setParameter("description", description);
        query.setParameter("subaccount", subaccount);
        
        dbTransactions = query.getResultList();
        
        return dbTransactions;
    }
    
    public List<Transactions> getSubAccTransactions(String toAcc){
        List<Transactions> allTransactions = new ArrayList<Transactions>();
        Query query = getEntityManager().createNamedQuery("getTransaction");
        query.setParameter("toaccs", toAcc);
        
        allTransactions = query.getResultList();
        
        return allTransactions;
    }
    
    public List<TransactionsSupport> getSubAccountBalanceMonth(Date queryDate,Accounts account,String reportPeriod){
        List<TransactionsSupport> allSupport = new ArrayList<TransactionsSupport>();
        double balance = 0,debit=0,credit=0;
       if(reportPeriod.equalsIgnoreCase("month")){
           df = new SimpleDateFormat("yyyy-MM");
       }
       else{
            df = new SimpleDateFormat("yyyy");
       }
         
        Query sumDebit = getEntityManager().createNamedQuery("getDebit");
        Query sumCredit = getEntityManager().createNamedQuery("getCredit");
        Query testQ = getEntityManager().createNamedQuery("getCredit");
        testQ.setParameter("subAcc", "Pass Book");
         testQ.setParameter("transdate", df.format(queryDate)+"%");
         //System.out.println("Sum Credit --> " + (Double)testQ.getSingleResult());
        // Since we loop via sub accounts per assets
       
        for(SubAccounts sub : account.getSubAccounts() ){
//        System.out.println("  Sub Account --> " + sub.getName() +" Balance --> " + sub.getBalance());
         TransactionsSupport tSupport = new TransactionsSupport();
        sumDebit.setParameter("subAcc",sub.getName());
        sumDebit.setParameter("transdate", df.format(queryDate)+"%");
        
        sumCredit.setParameter("subAcc",sub.getName());
        sumCredit.setParameter("transdate", df.format(queryDate)+"%");
         
         
        tSupport.setSubAccount(sub.getName());
        
        try{
            if(sumDebit.getSingleResult()== null || sumCredit.getSingleResult() == null){
                 if(sumDebit.getSingleResult()== null ){
           // System.out.println("No debit result for -->" + sub.getName());
            debit = 0;
            
                      }
                 if(sumCredit.getSingleResult() == null){
           // System.out.println("No credit result for -->" + sub.getName());
            credit = 0;
                          }
            }
       
        
        else{
            debit =(Double)sumDebit.getSingleResult();
            credit =(Double)sumCredit.getSingleResult();
           
        }
        
         }
        catch(Exception ex){
            ex.printStackTrace();
        }
        //Check account type before setting balance
        if(account.getName().equalsIgnoreCase("Assets") || account.getName().equalsIgnoreCase("Expenses")){
          balance =  (debit  - credit);
        }
        else if(account.getName().equalsIgnoreCase("Liabilities") || account.getName().equalsIgnoreCase("Equity") || account.getName().equalsIgnoreCase("Income")  ){
            balance =  (credit - debit);
        }
        
         
        tSupport.setBalance(balance+"");
        tSupport.setDebit(debit+"");
         tSupport.setCredit(credit+"");
       //  System.out.println("Sub Acc --> "+ tSupport.getSubAccount() + "Debit --> " + tSupport.getDebit() + "Credit --> " + tSupport.getCredit());
        allSupport.add(tSupport);
        
       
       credit = 0;
       debit = 0;
       
        
        }
      //System.out.println("Loops -->" + i);
       
     return allSupport;
    }
     
    public List<TransactionsSupport>  getBalanceByRange(Date startDate , Date endDate,Accounts account){
         List<TransactionsSupport> allSupport = new ArrayList<TransactionsSupport>();
         double balance = 0,debit=0,credit=0;
         df = new SimpleDateFormat("yyyy-MM");
         
        Query sumDebit = getEntityManager().createNamedQuery("getDebitRange");
        Query sumCredit = getEntityManager().createNamedQuery("getCreditRange");
       
        // Since we loop via sub accounts per assets
      
        for(SubAccounts sub : account.getSubAccounts() ){
       
      
      
       //Support bean
         TransactionsSupport tSupport = new TransactionsSupport();
        sumDebit.setParameter("subAcc",sub.getName());
        sumDebit.setParameter("fromdate", startDate);
        sumDebit.setParameter("todate", endDate);
        
        sumCredit.setParameter("subAcc",sub.getName());
        sumCredit.setParameter("fromdate",startDate);
        sumCredit.setParameter("todate", endDate);
        
         
         
        tSupport.setSubAccount(sub.getName());
        
        
        if(sumDebit.getSingleResult()== null ){
            debit = 0;
            
        }
        else if(sumCredit.getSingleResult() == null){
            credit = 0;
        }
        else{
            debit =(Double)sumDebit.getSingleResult();
            credit =(Double)sumCredit.getSingleResult();
           
        }
        
         
        //Check account type before setting balance
        if(account.getName().equalsIgnoreCase("Assets") || account.getName().equalsIgnoreCase("Expenses")){
          balance =  (debit  - credit);
        }
        else if(account.getName().equalsIgnoreCase("Liabilities") || account.getName().equalsIgnoreCase("Equity") || account.getName().equalsIgnoreCase("Income")  ){
            balance =  (credit - debit);
        }
        
         
        tSupport.setBalance(balance+"");
         tSupport.setDebit(debit+"");
         tSupport.setCredit(credit+"");
        allSupport.add(tSupport);
        
       
       credit = 0;
       debit = 0;
        
        }
         return allSupport;
    }
    
    //get transactions to be deleted
    public List<Transactions> delTransactions(long transId)
    {
        List<Transactions> delTrans = new ArrayList<Transactions>();
        
        Query query = getEntityManager().createNamedQuery("getTrans");
        query.setParameter("transId", transId);
        
        delTrans = query.getResultList();
        
        return delTrans;
    }
    

    public TransactionsFacade() {
        super(Transactions.class);
    }

}
