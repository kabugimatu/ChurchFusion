/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Facades;

import Entities.SaccoBanks;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author kabugi
 */
@Stateless
public class SaccoBanksFacade extends AbstractFacade<SaccoBanks> {
    @PersistenceContext(unitName = "SaccoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SaccoBanksFacade() {
        super(SaccoBanks.class);
    }
     public boolean checkBankExists(String bankName){
        boolean bankExists = true;
        Query query = getEntityManager().createNamedQuery("checkBankName");
        query.setParameter("bankname", bankName);
        if(query.getResultList().isEmpty()){
            bankExists = false;
        }
        else{
            SaccoBanks bankDB = (SaccoBanks)query.getResultList().get(0);
            if(bankDB.isTrash()){
                bankExists = false;
            }
            else{
            bankExists = true;
            }
        }
        return bankExists;
    }
    
}
