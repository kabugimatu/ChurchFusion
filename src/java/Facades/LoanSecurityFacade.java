

package Facades;

import Entities.LoanSecurity;
import java.util.ArrayList;
import java.util.List;
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
public class LoanSecurityFacade extends AbstractFacade<LoanSecurity> {
    @PersistenceContext(unitName = "SaccoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LoanSecurityFacade() {
        super(LoanSecurity.class);
    }

    //search security with serial number 
    public boolean securityExists(String serialNumber)
    {
        boolean exists = false;
        List<LoanSecurity> securities = new ArrayList<LoanSecurity>();
        Query query = getEntityManager().createNamedQuery("searchSecurity");
        
        query.setParameter("serialnumber", serialNumber);
        securities = query.getResultList();
        if(!securities.isEmpty())
        {
           exists = true; 
        }
        return exists;
    }
    
}
