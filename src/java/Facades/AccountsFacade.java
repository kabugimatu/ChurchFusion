/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Facades;

import Entities.Accounts;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author kabugi & george - PearlSoft Technologies
 */
@Stateless
public class AccountsFacade extends AbstractFacade<Accounts> {
    @PersistenceContext(unitName = "SaccoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccountsFacade() {
        super(Accounts.class);
    }
    
    public Accounts getAccByName(String accName){
        Accounts accountDB = new Accounts();
        Query query = getEntityManager().createNamedQuery("getAccByName");
        query.setParameter("name", accName);
        
        accountDB = (Accounts)query.getSingleResult();
        
        return accountDB;
    }

}
