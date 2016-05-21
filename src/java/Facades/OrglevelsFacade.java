

package Facades;

import Entities.Organizations;
import Entities.Orglevels;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author kabugi
 */
@Stateless
public class OrglevelsFacade extends AbstractFacade<Orglevels> {
    @PersistenceContext(unitName = "SaccoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OrglevelsFacade() {
        super(Orglevels.class);
    }
    public List<Orglevels> getLevelsByOrg(Organizations organ){
        List<Orglevels> allLevels = new ArrayList<Orglevels>();
        try{
        Query query = getEntityManager().createNamedQuery("findByOrgId");
        query.setParameter("orgid", organ.getOrgid());
        allLevels = query.getResultList();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return allLevels;
    }
    public boolean checkLevelExists(Orglevels level){
        boolean exists = false;
        try{
        Query query = getEntityManager().createNamedQuery("findByName");
        query.setParameter("levelName", level.getLevelname());
        exists = !query.getResultList().isEmpty();
        }
        catch(Exception ex){
            exists = true;
        }
        return exists;
        
    }
    public Orglevels getLevelByName(String name){
        Orglevels level = null;
        try{
             Query query = getEntityManager().createNamedQuery("findByName");
        query.setParameter("levelName", name);
        level =(Orglevels) query.getResultList().get(0);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return level;
    }
}
