/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Facades;

import Entities.LoanTopup;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author george.gitere
 */
@Stateless
public class LoanTopupFacade extends AbstractFacade<LoanTopup> {
    @PersistenceContext(unitName = "SaccoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LoanTopupFacade() {
        super(LoanTopup.class);
    }
    
    // query loan topup by id
    public List<LoanTopup> findLoanTopup(String loanid)
    {
        List<LoanTopup> dbTopups = new ArrayList<LoanTopup>();
        try{
        Query query = getEntityManager().createNamedQuery("findTopup");
        query.setParameter("loanNumber", loanid);        
        dbTopups = query.getResultList();        
        }
        catch(Exception ex){}
        return dbTopups;
    }
    
}
