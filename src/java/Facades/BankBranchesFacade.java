/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Facades;

import Entities.BankBranches;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author kabugi
 */
@Stateless
public class BankBranchesFacade extends AbstractFacade<BankBranches> {
    @PersistenceContext(unitName = "SaccoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BankBranchesFacade() {
        super(BankBranches.class);
    }
    
   
    
}
