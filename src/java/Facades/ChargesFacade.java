/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Facades;

import Entities.Charges;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author george
 */
@Stateless
public class ChargesFacade extends AbstractFacade<Charges> {
    @PersistenceContext(unitName = "SaccoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ChargesFacade() {
        super(Charges.class);
    }
    
    //check if charge exists
    public boolean checkCharges(String chargeName)
    {
        boolean exists = false;
        Query query = getEntityManager().createNamedQuery("checkCharge");
        
        query.setParameter("chargeName", chargeName);
        
        try
        {
           if(query.getSingleResult()!=null)
        {
            exists = true;
        } 
        }
        catch(Exception ex)
        {
           //do nothing 
        }
        
        
        
        return exists;
    }
    
     public List<Charges> getMandatoryCharges()
    {
       List<Charges> mandatoryCharges = new ArrayList<Charges>();
        Query query = getEntityManager().createNamedQuery("mandatoryCharges");
         query.setParameter("value", true);
        try
        {
           if(query.getResultList()!=null)
        {
            mandatoryCharges = query.getResultList();
        } 
           
        }
        catch(Exception ex)
        {
           ex.printStackTrace();
        }
        
        return mandatoryCharges;
    }
    
    
    
}
