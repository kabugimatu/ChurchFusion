

package Facades;

import Entities.MemberAccount;
import Entities.SharesSettings;
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
public class ShareSettingsFacade extends AbstractFacade<SharesSettings> {
    @PersistenceContext(unitName = "SaccoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ShareSettingsFacade() {
        super(SharesSettings.class);
    }
    
   
    
    

}
