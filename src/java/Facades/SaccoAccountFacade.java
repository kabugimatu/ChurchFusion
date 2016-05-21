

package Facades;

import Entities.SaccoAccount;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author George / Kabugi Pearl Soft Technologies 
 * All rights reserved
 */
@Stateless
public class SaccoAccountFacade extends AbstractFacade<SaccoAccount> {
    @PersistenceContext(unitName = "SaccoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SaccoAccountFacade() {
        super(SaccoAccount.class);
    }

}
