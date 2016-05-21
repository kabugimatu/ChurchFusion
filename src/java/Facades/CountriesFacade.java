

package Facades;

import Entities.Countries;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author kabugi
 */
@Stateless
public class CountriesFacade extends AbstractFacade<Countries> {
    @PersistenceContext(unitName = "SaccoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CountriesFacade() {
        super(Countries.class);
    }
    
    //Method to check if country exists
    public boolean checkCountryExists(Countries country){
        System.out.println("Niko hapa..");
        boolean exists = true;
        try{
        Query query = getEntityManager().createNamedQuery("findByCountry");
        query.setParameter("country", country.getCountry());
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
    
     public boolean checkDialCodeExists(Countries country){
        boolean exists = true;
        Query query = getEntityManager().createNamedQuery("findByDialingcode");
        query.setParameter("dialingcode", country.getDialingcode());
        if(query.getResultList().isEmpty()){
            exists = false;
        }
        else{
            exists = true;
        }
        return exists;
    }
}
