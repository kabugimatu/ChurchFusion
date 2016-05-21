/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Facades;

import Entities.LevelApprovers;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author kabugi
 */
@Stateless
public class LevelApproversFacade extends AbstractFacade<LevelApprovers> {
    @PersistenceContext(unitName = "SaccoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LevelApproversFacade() {
        super(LevelApprovers.class);
    }
    
}
