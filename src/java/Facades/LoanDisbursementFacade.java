/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Facades;

import Entities.LoanDisbursement;
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
 * @author kabugi
 */
@Stateless
public class LoanDisbursementFacade extends AbstractFacade<LoanDisbursement> {
    @PersistenceContext(unitName = "SaccoPU")
    private EntityManager em;
  SimpleDateFormat dateFormat ;
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LoanDisbursementFacade() {
        super(LoanDisbursement.class);
    }
    
    public List<LoanDisbursement> getDisbursementsByDate(Date dateQuery,Long bankId,Long branchId){
        List<LoanDisbursement> allDisbursements = new ArrayList<LoanDisbursement>();
       dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Query query = getEntityManager().createNamedQuery("searchByDate");
        try{
             query.setParameter("datedis", "%"+dateFormat.format(dateQuery)+"%");
              query.setParameter("bid", bankId);
         query.setParameter("brid", branchId);
             allDisbursements = query.getResultList();
            
        }
        catch(Exception ex){
            allDisbursements = new ArrayList<LoanDisbursement>();
        }
        System.out.println("Disbursements --> " + allDisbursements.size());
        
        return allDisbursements;
    }
    
    public List<LoanDisbursement> getDisbursementsByMonth(Date dateQuery,Long bankId,Long branchId){
        dateFormat = new SimpleDateFormat("yyyy-MM");
            List<LoanDisbursement> allDisbursements = new ArrayList<LoanDisbursement>();
         Query query = getEntityManager().createNamedQuery("searchByMonth");
         query.setParameter("month", dateFormat.format(dateQuery)+"%");
         query.setParameter("bid", bankId);
         query.setParameter("brid", branchId);
         try{
             allDisbursements = query.getResultList();
         }
         catch(Exception ex){
             allDisbursements = new ArrayList<LoanDisbursement>();
         }
         System.out.println("Result " + allDisbursements.size());
         return allDisbursements;
    }
    
     public List<LoanDisbursement> getDisbursementsByYear(Date dateQuery,Long bankId,Long branchId){
        dateFormat = new SimpleDateFormat("yyyy");
            List<LoanDisbursement> allDisbursements = new ArrayList<LoanDisbursement>();
         Query query = getEntityManager().createNamedQuery("searchByMonth");
         query.setParameter("month", dateFormat.format(dateQuery)+"%");
           query.setParameter("bid", bankId);
         query.setParameter("brid", branchId);
         try{
             allDisbursements = query.getResultList();
         }
         catch(Exception ex){
             allDisbursements = new ArrayList<LoanDisbursement>();
         }
         
         return allDisbursements;
    }
    
     public List<LoanDisbursement> getDisbursementsByChequeNumber(String chequeNumber){
      
            List<LoanDisbursement> allDisbursements = new ArrayList<LoanDisbursement>();
         Query query = getEntityManager().createNamedQuery("searchByCheque");
         query.setParameter("cheque",chequeNumber);
          
         try{
             allDisbursements = query.getResultList();
         }
         catch(Exception ex){
             allDisbursements = new ArrayList<LoanDisbursement>();
         }
         System.out.println("Result : " + allDisbursements.size());
         return allDisbursements;
    }
      public List<LoanDisbursement> getDisbursementsByDateRange(Date dateFrom, Date dateTo,Long bankId,Long branchId){
      dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            List<LoanDisbursement> allDisbursements = new ArrayList<LoanDisbursement>();
         Query query = getEntityManager().createNamedQuery("searchByDateRange");
         query.setParameter("dateFrom", dateFrom);
          query.setParameter("dateTo", dateTo);
          
         try{
             List<LoanDisbursement> dbDisbursement = query.getResultList();
          for(LoanDisbursement disb : dbDisbursement){
              if(disb.getBankBranchId().equals(branchId) && disb.getBankId().equals(bankId)){
                  allDisbursements.add(disb);
              }
          }
            
         }
         catch(Exception ex){
             allDisbursements = new ArrayList<LoanDisbursement>();
         }
         
         return allDisbursements;
    }
    
    
}
