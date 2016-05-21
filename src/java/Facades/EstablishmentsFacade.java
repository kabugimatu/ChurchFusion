

package Facades;

import Entities.Establishments;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author kabugi
 */
@Stateless
public class EstablishmentsFacade extends AbstractFacade<Establishments> {
    @PersistenceContext(unitName = "SaccoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EstablishmentsFacade() {
        super(Establishments.class);
    }
    
}
