/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Facades;

import Entities.DeletedTransaction;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author georgegitere
 */
@Stateless
public class DeletedTransactionFacade extends AbstractFacade<DeletedTransaction> {
    @PersistenceContext(unitName = "SaccoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DeletedTransactionFacade() {
        super(DeletedTransaction.class);
    }
    
}
