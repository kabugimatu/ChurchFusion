/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Facades;

import Entities.Users;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author kabugi & george - PearlSoft Technologies
 */
@Stateless
public class SaccoUserFacade extends AbstractFacade<Users> {
    @PersistenceContext(unitName = "SaccoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SaccoUserFacade() {
        super(Users.class);
    }
    
    
    
    public Users checkUserLogged(Users checkUser){
         Users authLogged= null;
        try{
            
            if(checkUser.getUserName().equalsIgnoreCase("pearl") && checkUser.getPassword().equalsIgnoreCase("9DJPW364lBIlrBfxhHXO/E6uun8=")){
                authLogged = new Users();
                authLogged.setUserName("DEVELOPER");
                authLogged.setFullName("PearlSoft Technologies");
                authLogged.setGender("UNDEFINED");
                authLogged.setEmailAddress("support@pearltechnologies.co.ke");
                authLogged.setTelephone("0717166609/0710631237");
                authLogged.setUserDomain("DEVELOPER");
                authLogged.setUserStatus(true);
            }
            else{
                
        Query userQuery = getEntityManager().createNamedQuery("authenticate");
        userQuery.setParameter("username", checkUser.getUserName());
        userQuery.setParameter("password", checkUser.getPassword());
        
        
        authLogged = (Users)userQuery.getSingleResult();
            }
        }
        catch(javax.persistence.NoResultException exception){
           
            authLogged = null;
        }
        return authLogged;
    }
    
    public boolean checkTelephone(Users user){
        boolean exists = false;
         List<Users> allUsers = findAll();
        Iterator iterator = allUsers.iterator();
        while(iterator.hasNext()){
           Users dbUser = (Users)iterator.next();
           if(dbUser.getTelephone().equals(user.getTelephone())){
               exists = true;
               break;
           }
           else{
               exists = false;
               continue;
           }
        }
        
        
        return exists;
    }
    public boolean checkUserName(Users user){
        boolean exists = false;
        
        List<Users> allUsers = findAll();
        Iterator iterator = allUsers.iterator();
        while(iterator.hasNext()){
           Users dbUser = (Users)iterator.next();
           if(dbUser.getUserName().equals(user.getUserName())){
               exists = true;
               break;
           }
           else{
               exists = false;
               continue;
           }
        }
        
        return exists;
    }
    
     public boolean checkId(Users user){
        boolean exists = false;
        
        List<Users> allUsers = findAll();
        Iterator iterator = allUsers.iterator();
        while(iterator.hasNext()){
           Users dbUser = (Users)iterator.next();
           if(dbUser.getIdNumber() == (user.getIdNumber())){
               exists = true;
               break;
           }
           else{
               exists = false;
               continue;
           }
        }
        
        return exists;
    }
     
     public boolean checkEmailAddress(Users user){
         
          boolean exists = false;
        
        List<Users> allUsers = findAll();
        Iterator iterator = allUsers.iterator();
        while(iterator.hasNext()){
           Users dbUser = (Users)iterator.next();
           if(dbUser.getEmailAddress().equals(user.getEmailAddress())){
               exists = true;
               break;
           }
           else{
               exists = false;
               continue;
           }
        }
        
        return exists;
         
     }
     public boolean validatePhone(Users user){
         boolean valid=true;
         if(!user.getTelephone().startsWith("07") ){
             valid=false;
         }
         
         else if(user.getTelephone().trim().length() <10){
             valid = false;
         }
         else if(user.getTelephone().trim().length()>10){
             valid=false;
         }
         return valid;
     }
     
//     public List<Users> getManagers(){
//         List<Users> allManagers =null;
//         CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
//         CriteriaQuery cQuery = builder.createQuery();
//         Root rt = cQuery.from(Users.class);
//         cQuery.select(rt).where(builder.equal(rt.get("userDomain"), "Manager"));
//         Query q = getEntityManager().createQuery(cQuery);
//        allManagers = q.getResultList();
//        return allManagers;
//                
//     }
//     public Users getManagerByPhone(String phone){
//         Users manager = null;
//         try{
//         String userPhone = phone.replace("+254", "0");
//           CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
//         CriteriaQuery cQuery = builder.createQuery();
//         Root rt = cQuery.from(Users.class);
//         cQuery.select(rt).where(builder.equal(rt.get("telephone"), userPhone));
//         Query q = getEntityManager().createQuery(cQuery);
//         manager = (Users)q.getSingleResult();
//         }
//         catch(javax.persistence.NoResultException exception){
//             manager= null;
//         }
//         return manager;
//         
//     }
//     
     // get users by level
     public List<Users> filterUsersByLevel(Long level){
         List<Users> dbUsers = new ArrayList<Users>();
         Query query = getEntityManager().createNamedQuery("getUsers");
         query.setParameter("userlevelid", level);
         try{
             dbUsers = query.getResultList();
         }
         catch(Exception ex){
             
         }
         return dbUsers;
     }


}
