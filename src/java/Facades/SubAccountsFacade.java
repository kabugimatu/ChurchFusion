/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Facades;

import Entities.Accounts;
import Entities.SubAccounts;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author kabugi & george - PearlSoft Technologies
 */
@Stateless
public class SubAccountsFacade extends AbstractFacade<SubAccounts> {
    @PersistenceContext(unitName = "SaccoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SubAccountsFacade() {
        super(SubAccounts.class);
    }
    
     public SubAccounts getSubAccByName(String subAccName){
        SubAccounts subAccountDB = new SubAccounts();
        Query query = getEntityManager().createNamedQuery("getSubAccByName");
        query.setParameter("name", subAccName);
        try
        {
            subAccountDB = (SubAccounts)query.getSingleResult(); 
        }
        catch(Exception ex)
        {
            subAccountDB = null;
        }
       
        
        return subAccountDB;
    }

}
