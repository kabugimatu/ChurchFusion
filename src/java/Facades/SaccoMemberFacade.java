

package Facades;

import Entities.SaccoMember;
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
public class SaccoMemberFacade extends AbstractFacade<SaccoMember> {
    @PersistenceContext(unitName = "SaccoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SaccoMemberFacade() {
        super(SaccoMember.class);
    }
    
    public List<SaccoMember> getMembersByBankInfo(Long id){
        List<SaccoMember> allMembers = new ArrayList<SaccoMember>();
        Query query = getEntityManager().createNamedQuery("searchByBankInfo");
        query.setParameter("bankid", id);
        query.setParameter("branchid", id);
        try{
            allMembers = query.getResultList();
        }
        catch(Exception ex){
            allMembers = new ArrayList<SaccoMember>();
        }
        
        return allMembers;
    }
    
    public SaccoMember getMemberByMemberNumber(String memberNumber){
        SaccoMember dbMember = null;
        Query query = getEntityManager().createNamedQuery("searchByMemberNumber");
        query.setParameter("mNumber", memberNumber);
        try{
            if(query.getResultList().isEmpty()){
                dbMember = null;
            }
            else{
                dbMember = (SaccoMember)query.getResultList().get(0);
            }
            
        }
        catch(Exception ex){
            dbMember = null;
        }
        
        return dbMember;
    }
    public List<SaccoMember> getMemberByBranchID(String branchId){
        Query query = getEntityManager().createNamedQuery("searchByBranch");
        query.setParameter("branchName", branchId);
        List<SaccoMember> dbMembers = new ArrayList<SaccoMember>();
        try{
           dbMembers = query.getResultList();
        }
        catch(Exception ex){
            dbMembers = new ArrayList<SaccoMember>();
        }
        System.out.println("getting ... " + dbMembers.size());
        return dbMembers;
    }
     //Check Member Number
    public SaccoMember searchByPayroll(String payrollNumber){
        SaccoMember saccoMember = null;
        try{
            Query query = getEntityManager().createNamedQuery("searchPayroll");
            query.setParameter("payrollnumber", payrollNumber);
            
            if(!query.getResultList().isEmpty()){
                saccoMember = (SaccoMember)query.getResultList().get(0);
            }
            else{
                saccoMember = null;
            }
            
        }
        catch(Exception ex){
            
        }
        
        
        return saccoMember;
    }
    
    //check if member exists
    public String checkMemberExists(SaccoMember member)
    {
        String exists = "";
        Query idQuery = getEntityManager().createNamedQuery("checkId");
        Query telQuery = getEntityManager().createNamedQuery("checkTel");
        Query emailQuery = getEntityManager().createNamedQuery("checkEmail");
        Query numberQuery = getEntityManager().createNamedQuery("checkNumber");
        idQuery.setParameter("idnumber", member.getIdNumber());
        telQuery.setParameter("tel", member.getPhoneNumber());
        emailQuery.setParameter("email", member.getEmail());
        
            numberQuery.setParameter("number", member.getMemberNumber());
       
        try{
        if(!idQuery.getResultList().isEmpty()){
            exists = "Member with that Id number Exists";
        }    
        
        else if(!telQuery.getResultList().isEmpty()){
             exists = "Member with that Telephone number Exists";
        }
//         else if(!emailQuery.getResultList().isEmpty()){
//             exists = "Member with that email address Exists";
//        }
         else if(!numberQuery.getResultList().isEmpty()){
             exists = "Member with that member number Exists";
         }
         else{
             exists = "no";
         }
        }
        catch(Exception ex){
            
        }
        return exists;
    
}
    public int generateMemberNumber(){
        int memberNumber = 0;
        List<SaccoMember> allMembers = findAll();
        if(allMembers.isEmpty()){
            
            memberNumber = 1;
        }
        else{
            memberNumber = allMembers.size()+ 1 ;
        }
        
        return memberNumber;
    }
    
    //search sacc member using id number or member number 
    public SaccoMember searchSaccoMember(String searchCriteria)
    {
        
        SaccoMember saccoMember = new SaccoMember();
        Query query = getEntityManager().createNamedQuery("searchMember");
        
        query.setParameter("searchQuery", searchCriteria);
        
        if(!query.getResultList().isEmpty())
        {
           saccoMember = (SaccoMember)query.getResultList().get(0);
        }
        else
        {
            saccoMember =null;
        }
        
        return saccoMember;
    }
    
    
    
}