/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Facades;

import Entities.SaccoDetails;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author george
 */
@Stateless
public class SaccoDetailsFacade extends AbstractFacade<SaccoDetails> {
    @PersistenceContext(unitName = "SaccoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SaccoDetailsFacade() {
        super(SaccoDetails.class);
    }
    
    public boolean checkTrial(){
        boolean status = false;
        
        return status;
    }
    
}
