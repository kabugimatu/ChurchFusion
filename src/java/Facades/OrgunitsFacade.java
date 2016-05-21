

package Facades;

import Entities.Organizations;
import Entities.Orglevels;
import Entities.Orgunits;
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
public class OrgunitsFacade extends AbstractFacade<Orgunits> {
    @PersistenceContext(unitName = "SaccoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OrgunitsFacade() {
        super(Orgunits.class);
    }
    public List<Orgunits> getUnitsByOrg(Organizations organ){
        List<Orgunits> allUnits = new ArrayList<Orgunits>();
        try{
        Query query = getEntityManager().createNamedQuery("getByOrg");
        query.setParameter("orgid", organ.getOrgid());
        allUnits = query.getResultList();
//        System.out.println(allUnits);
        }
        catch(Exception ex){
            
        }
        return allUnits;
    }
    
    public List<Orgunits> getUnitsByLevel(Orglevels level){
        
        Query query = getEntityManager().createNamedQuery("getByLevel");
        query.setParameter("levelid", level.getHlevel());
        try{
            return query.getResultList();
        }
        catch(Exception ex){
            ex.printStackTrace();
           return new ArrayList<Orgunits>();
        }
        
        
    }
    public List<Orgunits> getChildUnits(Orgunits unit){
       
        Query query = getEntityManager().createNamedQuery("getChildren");
        query.setParameter("unitid", unit.getOrgunitid());
        try{
            return query.getResultList();
        }
        catch(Exception ex){
            ex.printStackTrace();
            return new ArrayList<Orgunits>();
            
        }
        
        
        
    }
    
}
