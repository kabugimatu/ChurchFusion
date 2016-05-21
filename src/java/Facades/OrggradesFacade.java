

package Facades;

import Entities.Organizations;
import Entities.Orggrades;
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
public class OrggradesFacade extends AbstractFacade<Orggrades> {
    @PersistenceContext(unitName = "SaccoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OrggradesFacade() {
        super(Orggrades.class);
    }
    public List<Orggrades> getGradesByOrg(Organizations organ){
        List<Orggrades> allGrades = new ArrayList<Orggrades>();
        try{
        Query query = getEntityManager().createNamedQuery("getByOrgan");
        query.setParameter("orgid", organ.getOrgid());
        allGrades = query.getResultList();
        }
        catch(Exception ex){
            
        }
        return allGrades;
    }
}
