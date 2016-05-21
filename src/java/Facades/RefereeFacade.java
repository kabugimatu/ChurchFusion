

package Facades;

import Entities.Referee;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author George / Kabugi Pearl Soft Technologies 
 * All rights reserved
 */
@Stateless
public class RefereeFacade extends AbstractFacade<Referee> {
    @PersistenceContext(unitName = "SaccoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RefereeFacade() {
        super(Referee.class);
    }

}
