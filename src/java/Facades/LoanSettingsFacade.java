/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Facades;

import Entities.LoanSettings;
import Entities.LoanType;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author george
 */
@Stateless
public class LoanSettingsFacade extends AbstractFacade<LoanSettings> {
    @PersistenceContext(unitName = "SaccoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LoanSettingsFacade() {
        super(LoanSettings.class);
    }
    
    public LoanSettings getLoanType(String type){
        LoanSettings loanType = null;
        Query dbQuery = getEntityManager().createNamedQuery("checkType");
         dbQuery.setParameter("typename", type);
        
         if(!dbQuery.getResultList().isEmpty()){
            loanType = (LoanSettings)dbQuery.getResultList().get(0);
         }
         
         else {
              loanType = new LoanSettings();
         }
         
        return loanType;
    }
    
    public boolean checkTypeExists(LoanSettings typeSettings){
         boolean exists = true;
         Query dbQuery = getEntityManager().createNamedQuery("checkType");
         dbQuery.setParameter("typename", typeSettings.getLoanType());
         
         if(!dbQuery.getResultList().isEmpty()){
             exists = true;
         }
         
         else {
             exists = false;
         }
         
         
         return exists;
                 
     }
    
}
