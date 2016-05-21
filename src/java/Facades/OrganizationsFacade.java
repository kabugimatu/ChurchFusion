

package Facades;

import Entities.Organizations;
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
public class OrganizationsFacade extends AbstractFacade<Organizations> {
    @PersistenceContext(unitName = "SaccoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OrganizationsFacade() {
        super(Organizations.class);
    }
    public Organizations findBId(Organizations organ){
        Organizations organization = null;
        Query query = getEntityManager().createNamedQuery("findByOrgid");
        query.setParameter("orgid", organ.getOrgid());
        
       organization = (Organizations)query.getResultList().get(0);
       return organization;
    }
    
    public List<Organizations> getOrgTypes(String type){
        List<Organizations> orgTypes = new ArrayList<Organizations>();
         Query query = getEntityManager().createNamedQuery("findByOrgtype");
         query.setParameter("orgtype", type);
         try{
          orgTypes= query.getResultList();
           }
         catch(Exception ex){
                  orgTypes = new ArrayList<Organizations>();
                 }
         
         return orgTypes;
    }
    public List<Organizations> getSubOrganizations(Organizations organ){
        List<Organizations> subOrgs = new ArrayList<Organizations>();
        Query query = getEntityManager().createNamedQuery("findChildren");
        query.setParameter("parentid", organ.getOrgid());
        try{
            subOrgs = query.getResultList();
        }
        catch(Exception ex){
            subOrgs = new ArrayList<Organizations>();
        }
        
        return subOrgs;
        
    }
}
