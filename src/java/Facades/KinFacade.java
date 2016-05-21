

package Facades;

import Entities.Kin;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author George / Kabugi Pearl Soft Technologies 
 * All rights reserved
 */
@Stateless
public class KinFacade extends AbstractFacade<Kin> {
    @PersistenceContext(unitName = "SaccoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public KinFacade() {
        super(Kin.class);
    }

}
