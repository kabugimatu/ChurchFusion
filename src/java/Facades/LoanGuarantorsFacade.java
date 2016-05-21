

package Facades;

import Entities.LoanApproval;
import Entities.LoanGuarantors;
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
public class LoanGuarantorsFacade extends AbstractFacade<LoanGuarantors> {
    @PersistenceContext(unitName = "SaccoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LoanGuarantorsFacade() {
        super(LoanGuarantors.class);
    }
    
    
    // get user logged pending loan approvals
    public List<LoanGuarantors> memberGuarantors(String memberNumber) {        
        List<LoanGuarantors> guarantors = new ArrayList<LoanGuarantors>();
        Query query = getEntityManager().createNamedQuery("fetchGuarantors");
        query.setParameter("memberNumber", memberNumber);        
        try {
            guarantors = query.getResultList();            
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        return guarantors;
    }

}
