

package Facades;

import Entities.Organizations;
import Entities.Orgparameters;
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
public class OrgparametersFacade extends AbstractFacade<Orgparameters> {
    @PersistenceContext(unitName = "SaccoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OrgparametersFacade() {
        super(Orgparameters.class);
    }
    
     public List<Orgparameters> getParByOrg(Organizations organ){
        List<Orgparameters> allParams = new ArrayList<Orgparameters>();
        try{
        Query query = getEntityManager().createNamedQuery("getByOrgani");
        query.setParameter("orgid", organ.getOrgid());
        allParams = query.getResultList();
        }
        catch(Exception ex){
            
        }
        return allParams;
    }
    
}
