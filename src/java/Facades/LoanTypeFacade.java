/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Facades;

import Entities.LoanType;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author kabugi
 */
@Stateless
public class LoanTypeFacade extends AbstractFacade<LoanType> {
    @PersistenceContext(unitName = "SaccoPU")
    private EntityManager em;
     
     @Override
    protected EntityManager getEntityManager() {
        return em;
    }
     
     public LoanTypeFacade(){
         super(LoanType.class);
     }
     
     public boolean checkTypeExists(LoanType typeLoan){
         boolean exists = true;
         Query dbQuery = getEntityManager().createNamedQuery("checkType");
         dbQuery.setParameter("typename", typeLoan.getTypeLoan());
         
         if(!dbQuery.getResultList().isEmpty()){
             exists = true;
         }
         
         else {
             exists = false;
         }
         
         
         return exists;
                 
     }
}
