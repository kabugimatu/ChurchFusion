

package Facades;

import Entities.Countries;
import Entities.Towns;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author kabugi
 */
@Stateless
public class TownsFacade extends AbstractFacade<Towns> {
    @PersistenceContext(unitName = "SaccoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TownsFacade() {
        super(Towns.class);
    }
    
      public boolean checkTownExists(Towns town,Countries sCountry){
        boolean exists = true;
        try{
        Query query = getEntityManager().createNamedQuery("findByTownNameID");
        query.setParameter("townname", town.getTownname());
        query.setParameter("countryid", sCountry);
        
        if(query.getResultList().isEmpty()){
            exists = false;
        }
        else{
            exists = true;
        }
        }
        catch(Exception ex){
            ex.printStackTrace();
            exists = true;
        }
        return exists;
    }
}
