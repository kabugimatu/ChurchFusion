

package Facades;

import Entities.Business;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author George / Kabugi Pearl Soft Technologies 
 * All rights reserved
 */
@Stateless
public class BusinessFacade extends AbstractFacade<Business> {
    @PersistenceContext(unitName = "SaccoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BusinessFacade() {
        super(Business.class);
    }

    public Business getBusinessByMemberNumber(String memberNumber){
        Business dbBusiness = null;
        Query query = getEntityManager().createNamedQuery("getBusinessByNumber");
        query.setParameter("membernumber", memberNumber);
        try{
            dbBusiness = (Business)query.getSingleResult();
        }
        catch(Exception ex){
            dbBusiness = null;
        }
        
        
        return dbBusiness;
    }
    
}
