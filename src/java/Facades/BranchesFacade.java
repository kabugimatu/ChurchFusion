

package Facades;

import Entities.CompanyBranches;
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
public class BranchesFacade extends AbstractFacade<CompanyBranches> {
    @PersistenceContext(unitName = "SaccoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    
    public boolean checkBranchExists(CompanyBranches branch){
        boolean exists = true;
        Query query = getEntityManager().createNamedQuery("checkBranch");
        query.setParameter("branchname", branch.getBranchname());
        if(query.getResultList().isEmpty()){
            exists = false;
        }
        else{
            exists = true;
        }
        return exists;
    }
    public BranchesFacade() {
        super(CompanyBranches.class);
    }

}
