/*
 * This is the main Organizations Controller used to add new Organizations, 
    and basic organization settings. .
 */

package Controllers;
 
import Entities.Countries;
import Entities.Establishments;
import Entities.Organizations;
import Entities.Orggrades;
import Entities.Orglevels;
import Entities.Orgparameters;
import Entities.Orgunits;
import Facades.CountriesFacade;
import Facades.EstablishmentsFacade;
import Facades.JobsFacade;
import Facades.OrganizationsFacade;
import Facades.OrggradesFacade;
import Facades.OrglevelsFacade;
import Facades.OrgparametersFacade;
import Facades.OrgunitsFacade;
import Facades.TownsFacade;
import SupportBeans.LevelSupport;

import com.sun.xml.messaging.saaj.util.ByteInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;

/**
 *
 * @author kabugi
 * For NetComm Information Services
 */
@ManagedBean(name = "orgController")
@SessionScoped
public class OrganizationsController {
    
    //Stateless Enterprise Java Beans
    
    @EJB 
    private CountriesFacade countriesFacade;
    @EJB
    private EstablishmentsFacade estFacade;
    @EJB
    private JobsFacade jobFacade;
    @EJB
    private OrggradesFacade orgFacade;
    @EJB
    private OrglevelsFacade orgLevelFacade;
    @EJB
    private OrgparametersFacade orgParamFacade;
    @EJB
    private OrgunitsFacade orgUnitFacade;
    @EJB
    private OrganizationsFacade organizationFacade;
    @EJB
    private TownsFacade townsFacade;
    
    private Long countryId,orgId,loadOrgID,loadLevelID,
            loadChildOrgID =null,loadParentID,subOrgCountryID,tempOrgID,changeOrgID,changeLevelID,changeUnitID,parentOrgID;
    boolean showGroupOrgs,showCompanyOrgs,showSubOrgs,levelAdded =false,agreeTerms,continueAdd,addSucceed,vUnitAdd=false;
    private StreamedContent image;
    private String tempPath = null,orgType,oldLevelName,oldUnitName;
    private TreeNode selectedNode,selectedLoadNode,parentNode; 
    private TreeNode root,rootLoad,parentLoadNode,childOrgNode,levelNode,unitNode,cUnitNode,childHeadNode;
     byte[] bytes ;
    
    //Entity Beans
    private Countries newCountry,editCountry,selectCountry;
    private Organizations newOrganization,loadOrganization,subOrganization;
    private Orgparameters newOrgParam,editOrgParam,selectOrgParams;
    private Orggrades newOrgGrade,editOrgGrade,selectOrgGrade;
    private Orglevels newOrgLevel,deleteLVL,selectOrglevels;
    private Orgunits newOrgUnit,deleteUnit,selectUnit;
//Data Models   
    private DataModel<Organizations> subOrganizationDM;
    private DataModel<Countries> countriesDM ;
    private DataModel<Orgparameters> tempOrgParamDM;
    private DataModel<Orggrades> tempOrgGradeDM;
    private DataModel<Organizations> dbOrgsDM;
    private DataModel<Orgparameters> loadParametersDM;
    private DataModel<Establishments> loadEstablishmentsDM;
    private DataModel<Orggrades> loadGradeDM;
    private DataModel<Orglevels> orgLevelDM;
    private DataModel<Orgunits> orgUnitsDM;
    private DataModel<Orglevels> tempOrgLevelsDM;
    private DataModel<Orgunits> temOrgUnitsDM;
    //LISTS
    private List<Countries> allCountries;
    private List<Orgparameters> tempOrgPars;
    private List<Orggrades> tempOrgGrades;
    private List<Organizations> allGroupOrgs;
    private List<Organizations> companyOrgs;
     private List<Organizations> subsidiaryOrgs;
    private List<Orglevels> childLevels;
    private List<Orglevels> tempOrgLevels;
    private List<Orgunits> tempOrgUnits;
    private List<Orglevels> allHlevels;
    private List<Organizations> childOrgs;
    private List<LevelSupport> lvlSupport;
    private List<Orgunits> allLoadUnits ,allTempUnits;
  
            
    
   public OrganizationsController(){
       
       //Variable INIT
   
       
       //FACADES INITIALIZE
        this.townsFacade = new TownsFacade();
        this.organizationFacade = new OrganizationsFacade();
        this.orgUnitFacade = new OrgunitsFacade();
        this.orgParamFacade = new OrgparametersFacade();
        this.orgLevelFacade = new OrglevelsFacade();
        this.orgFacade = new OrggradesFacade();
        this.jobFacade = new JobsFacade();
        this.estFacade = new EstablishmentsFacade();
        this.countriesFacade = new CountriesFacade();
        
        //ENTITY INIT
        this.newCountry =  new Countries();
        this.editCountry = new Countries();
        this.selectCountry = new Countries();
        this.newOrganization = new Organizations();
        this.subOrganization = new Organizations();
        this.loadOrganization = new Organizations();
        this.newOrgParam = new Orgparameters();
        this.newOrgGrade = new Orggrades();
        this.newOrgLevel = new Orglevels();
        this.newOrgUnit = new Orgunits();
        this.editOrgParam = new Orgparameters();
        this.selectOrgParams = new Orgparameters();
        this.editOrgGrade = new Orggrades();
        this.selectOrgGrade = new  Orggrades();
        //DM INITIALIZE
        this.subOrganizationDM = new ListDataModel<Organizations>();
        this.countriesDM = new ListDataModel<Countries>();
        this.tempOrgParamDM = new ListDataModel<Orgparameters>();
        this.tempOrgGradeDM = new ListDataModel<Orggrades>();
        this.dbOrgsDM = new ListDataModel<Organizations>();
        this.loadParametersDM = new ListDataModel<Orgparameters>();
        this.loadEstablishmentsDM = new ListDataModel<Establishments>();
        this.loadGradeDM = new ListDataModel<Orggrades>();
        this.orgLevelDM = new ListDataModel<Orglevels>();
        this.tempOrgLevelsDM = new ListDataModel<Orglevels>();
        this.temOrgUnitsDM = new ListDataModel<Orgunits>();
        //LISTS INIT
        this.allCountries = new ArrayList<Countries>();
        this.tempOrgPars = new ArrayList<Orgparameters>();
        this.tempOrgGrades = new ArrayList<Orggrades>();
        this.allGroupOrgs = new ArrayList<Organizations>();
        this.companyOrgs = new ArrayList<Organizations>();
        this.childLevels = new ArrayList<Orglevels>();
        this.tempOrgLevels = new ArrayList<Orglevels>();
        this.tempOrgUnits = new ArrayList<Orgunits>();
        this.subsidiaryOrgs = new ArrayList<Organizations>();
        this.allHlevels = new ArrayList<Orglevels>();
        this.childOrgs = new ArrayList<Organizations>();
        this.lvlSupport = new ArrayList<LevelSupport>();
        this.allLoadUnits = new ArrayList<Orgunits>();
        this.allTempUnits = new ArrayList<Orgunits>();
   }
 
   
     public void handleFileUpload(FileUploadEvent event) {
       FacesContext ctx = FacesContext.getCurrentInstance();
       try{
            String path = "";
           Properties sysProps = System.getProperties();
         
            String fineName = "";
           String fileName = event.getFile().getFileName();
          
           fineName = newOrganization.getOrgname()+fileName.substring(fileName.lastIndexOf("."));
           
           
            File logoDirectory = new File(sysProps.getProperty("user.home")+ sysProps.getProperty("file.separator")+"OrgData"+sysProps.getProperty("file.separator")+"OrgLogos");
           if(logoDirectory.exists()){
                path = sysProps.getProperty("user.home")+sysProps.getProperty("file.separator")+"OrgData"+sysProps.getProperty("file.separator")+"OrgLogos"+sysProps.getProperty("file.separator") +fineName;
            }
            else{
                logoDirectory.mkdirs();
                
                path = sysProps.getProperty("user.home")+sysProps.getProperty("file.separator")+"OrgData"+sysProps.getProperty("file.separator")+"OrgLogos"+sysProps.getProperty("file.separator") +fineName;
            }
            
         
            File logoFile = new File(path);
                logoFile.createNewFile();
                
                 if (logoFile.exists()) {
                    InputStream inputstream = event.getFile().getInputstream();
               FileOutputStream outputStream = new FileOutputStream(logoFile);
               int read = 0;
               int i = (int)event.getFile().getSize();
               bytes = new byte[i];
               while ((read = inputstream.read(bytes)) != -1) {
                   outputStream.write(bytes, 0, read);
               }
                     outputStream.flush();
                    outputStream.close();
                    //DB Blob starts here
                    //Serializable data = bytes;
                    if(loadOrganization.getOrgid()!=null){
                        
                        loadOrganization.setLogo(bytes);
                        organizationFacade.edit(loadOrganization);
                    }
                    else{
                     
                    newOrganization.setLogo(bytes);
                   // organizationFacade.create(newOrganization);
                    }
                      
//                    if(loadOrganization.getOrgid()!=null){
//                      
//                         loadOrganization.setLogo(fileContent);
//                         organizationFacade.edit(loadOrganization);
//                    }
//                    else{
//                         newOrganization.setLogo(fileContent);
//                    }
//                   
                  //logoFile.delete();
                    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Logo Uploaded Successfully !", ""));
                  
                  tempPath = path;
               

           }
       }
       catch(Exception ex){
           ex.printStackTrace();
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Could Not Upload Logo !", ""));
           
       }
         
         
         
        
    }
    
     public void addCompany(){
         FacesContext ctx  =FacesContext.getCurrentInstance();
         try{
             subOrganization.setParentOrgid(loadOrganization);
             subOrganization.setOrgtype("Company");
         organizationFacade.create(subOrganization);
         ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Company Added successfully ! ", ""));
         }
         catch(Exception ex){
              ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Process failed ", ""));
         }
         subOrganization = new Organizations();
     }
     public void prepareAddOrg(){
       //Check if agree terms and condition checkbox is checked.
        FacesContext ctx = FacesContext.getCurrentInstance();
       if(!agreeTerms){
           continueAdd = false;
           ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "You must  accept terms and conditions before contintuing ", ""));
       }
       else{
           continueAdd = true;
       }
       
       
   }
     public void onNodeSelect(NodeSelectEvent event){
         String node = event.getTreeNode().getData().toString();
         System.out.println("Selected node --> " + node);
         
             
             vUnitAdd = true;
         
       
        
     }
     public void onNodeExpand(NodeExpandEvent event) {
        String node = event.getTreeNode().getData().toString();
        
    }

    public void onNodeCollapse(NodeCollapseEvent event) {
        String node = event.getTreeNode().getData().toString();
    }
    
    
     public String showOrganization(){
       String page = "viewOrganization.faces";
       loadOrganization = dbOrgsDM.getRowData();
       rootLoad = new DefaultTreeNode("Root", null);
        parentLoadNode = new DefaultTreeNode("orgs", loadOrganization.getOrgname(), rootLoad);
        //System.out.println(loadOrganization.getOrganizationsList());
        for(Orgunits unit : orgUnitFacade.getUnitsByOrg(loadOrganization)){
            if(unit.getParentOrgunitid() ==null){
               constructTree(unit, parentLoadNode);
            }
        }
        // System.out.println("All leveles " + loadOrganization.getOrglevelsList() );
       rootLoad.setExpanded(true);
         parentLoadNode.setExpanded(true);
       return page;
   } 
     
     //Recursive Method to construct tree
    public TreeNode constructTree(Orgunits parentUnit ,TreeNode parentNode){
        TreeNode unitChildNode = new DefaultTreeNode("units",parentUnit.getHlevel().getLevelname() +" : " +parentUnit.getOrgunitname(),parentNode);
        for(Orgunits childUnit : orgUnitFacade.getChildUnits(parentUnit)){
            TreeNode childUnit2 = constructTree(childUnit, unitChildNode);
        }
        return unitChildNode;
    }

   
  
      public void addOrgLevel(){
           FacesContext ctx = FacesContext.getCurrentInstance();
          try{
              
       boolean levelAdded = false;
            for(Orglevels level : orgLevelFacade.findAll()){
                if(level.getLevelname().equalsIgnoreCase(newOrgLevel.getLevelname())){
                     levelAdded = true;
                    break;
                }
            } 
            if(levelAdded){
                 ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Level already added", null));
            }
           
            else{
//             //   int levelIntID =orgLevelFacade.getLevelsByOrg(loadOrganization).size()+1;;
//               
//                 String levelString =levelIntID + "";
//                 Long levelLong = Long.parseLong(levelString);
//                 newOrgLevel.setHlevel(levelLong);
//              // newOrgLevel.setOrgid(loadOrganization);
//              
//                // int levelId = 0;
           
                orgLevelFacade.create(newOrgLevel);
               
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Level added successfully ", null));
            }
           
              
          }
          catch(Exception ex){
              ex.printStackTrace();
          }
         
             newOrgLevel = new Orglevels();
        }
      
//      public void addOrgUnits(){
//          FacesContext ctx = FacesContext.getCurrentInstance();
//          try{
//              
//       boolean unitAdded = false;
//       boolean unitEqLevel = false;
//       Orglevels parentLevel = null;
//       Orgunits parentUnit = null;
//       for(Orglevels loadLevel : loadOrganization.getOrglevelsList()){
//           if(selectedLoadNode.getData().toString().equalsIgnoreCase(loadLevel.getLevelname())){
//               parentLevel = loadLevel;
//               break;
//           }
//       }
//        for(Orgunits loadUnit : loadOrganization.getOrgunitsList()){
//           if(selectedLoadNode.getData().toString().equalsIgnoreCase(loadUnit.getOrgunitname())){
//               parentUnit = loadUnit;
//               break;
//           }
//       }
//       if(parentLevel!=null){
//            for(Orgunits unit : parentLevel.getOrgunitsList()){
//                if(unit.getOrgunitname().equalsIgnoreCase(newOrgUnit.getOrgunitname())){
//                    for(TreeNode node : selectedLoadNode.getChildren()){
//                        if(node.getData().toString().equalsIgnoreCase(newOrgUnit.getOrgunitname())){
//                            unitAdded = true;
//                            break;
//                        }
//                    }
//                     
//                    break;
//                }
//            } 
//       }
//       else if(parentUnit!=null){
//             for(Orgunits unit : parentUnit.getOrgunitsList()){
//                if(unit.getOrgunitname().equalsIgnoreCase(newOrgUnit.getOrgunitname())){
//                    for(TreeNode node : selectedLoadNode.getChildren()){
//                        if(node.getData().toString().equalsIgnoreCase(newOrgUnit.getOrgunitname())){
//                            unitAdded = true;
//                            break;
//                        }
//                    }
//                     
//                    break;
//                }
//            }
//       }
//            for(Orglevels level : loadOrganization.getOrglevelsList()){
//                if(level.getLevelname().equalsIgnoreCase(newOrgUnit.getOrgunitname())){
//                     unitEqLevel = true;
//                    break;
//                }
//            }
//            
//            if(unitAdded){
//                 ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unit already added", null));
//            }
//            else if(unitEqLevel){
//                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Parent level already added", null));
//            }
//            else if(selectedLoadNode.getData().toString().equalsIgnoreCase(loadOrganization.getOrgname())){
//                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select a Level or Unit", null));
//            }
//            else if(selectedLoadNode.getData().toString().equalsIgnoreCase(newOrgUnit.getOrgunitname())){
//                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please Enter a different unit name", null));
//            }
//            else{
//                 if(parentLevel!=null){
//                     newOrgUnit.setHlevel(parentLevel);
//                     
//                 }
//                 else if(parentUnit!=null){
//                     newOrgUnit.setParentOrgunitid(parentUnit);
//                      
//                 }
//                  if(newOrgUnit.getStatus().equalsIgnoreCase("true")){
//                             newOrgUnit.setStatus("Active");
//                         }
//                         else{
//                             newOrgUnit.setStatus("InActive");
//                         }
//                 newOrgUnit.setOrgid(loadOrganization);
//                 orgUnitFacade.create(newOrgUnit);
//                 
//               ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Unit added  successfully", null));
//            }
//            parentLevel =null;
//            parentUnit = null;
//            
//          }
//          catch(Exception ex){
//              ex.printStackTrace();
//          }
//          loadRoot();
//           organizationFacade.edit(loadOrganization);
//          newOrgUnit = new  Orgunits();
//      }
//       
   //LOAD ADD ORG
   public String loadAddOrg(){
       String page = "newOrganization.faces";
      
       return page;
   }
   public void selectOrgUnits(){
       selectUnit = orgUnitsDM.getRowData();
       oldUnitName = selectUnit.getOrgunitname();
       
   }
   public void updateOrgUnit(){
       FacesContext ctx = FacesContext.getCurrentInstance();
       try{
        if(changeLevelID==null){
           ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select level", ""));
       }
        else if(changeUnitID!=null && !selectUnit.getOrgunitsList().isEmpty()){
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please remove sub-units first", ""));
        }
        else if(changeUnitID!=null && selectUnit.getOrgunitname().equalsIgnoreCase(orgUnitFacade.find(changeUnitID).getOrgunitname())){
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unit cannot be parent to itself", ""));
        }
       else{
           Orglevels level = orgLevelFacade.find(changeLevelID);
           selectUnit.setHlevel(level);
           selectUnit.setOrgid(loadOrganization);
           
          
           if(selectUnit.getStatus().equalsIgnoreCase("True")){
               selectUnit.setStatus("Active");
           }
           else{
               selectUnit.setStatus("InActive");
           }
           
           orgUnitFacade.edit(selectUnit);
          
           ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Unit Updated successfully", ""));
        }
       }
       catch(Exception ex){
           ex.printStackTrace();
       }
           selectUnit = new Orgunits();
           changeLevelID = null;
           changeUnitID = null;
   }
   public void deleteOrgUnit(){
       FacesContext ctx = FacesContext.getCurrentInstance();
       if(!selectUnit.getOrgunitsList().isEmpty()){
           ctx.addMessage(null, new  FacesMessage(FacesMessage.SEVERITY_ERROR, "Remove Sub units first", ""));
       }
       else{
           
           orgUnitFacade.remove(selectUnit);
           ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Unit removed successfully", ""));
       }
       selectUnit = new Orgunits();
   }
   public void selectOrgLevel(){
       selectOrglevels = orgLevelDM.getRowData();
       oldLevelName = selectOrglevels.getLevelname();
       
   }
   public void updateOrgLevel(){
       FacesContext ctx = FacesContext.getCurrentInstance();
        try{
            boolean exists = true;
            if(!oldLevelName.equalsIgnoreCase(selectOrglevels.getLevelname())){
               exists = orgLevelFacade.checkLevelExists(selectOrglevels);
               if(exists){
                     ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Level already added", ""));
               }
               else{
                     
         orgLevelFacade.edit(selectOrglevels);
           ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User level update successful !", ""));
               }
            }
            else{
                orgLevelFacade.edit(selectOrglevels);
           ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User level update successful !", ""));
               }
          
           
            
       }
       catch(Exception ex){
           ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Update User Level failed.", ""));
       }
        selectOrglevels = new Orglevels();
   }
   public void deleteLevel(){
         FacesContext ctx = FacesContext.getCurrentInstance();
        try{
         
       orgLevelFacade.remove(selectOrglevels);
       
        ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User Level deleted successfully.", ""));
            
        }
        catch(Exception ex){
             ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: Please remove dependent loan setting first", ""));
        }
       selectOrglevels = new Orglevels();
   }
   public void updateOrgGrade(){
       FacesContext ctx = FacesContext.getCurrentInstance();
       try{
           loadOrganization.getOrggradesList().remove(editOrgGrade);
           orgFacade.edit(editOrgGrade);
           loadOrganization.getOrggradesList().add(editOrgGrade);
           organizationFacade.edit(loadOrganization);
           ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Organization update successful !", ""));
       }
       catch(Exception ex){
           ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Update Org Grade failed.", ""));
       }
   }
   public void addOrgGrades(){
       FacesContext ctx = FacesContext.getCurrentInstance();
       try{
            newOrgGrade.setOrgid(loadOrganization);
       
       loadOrganization.getOrggradesList().add(newOrgGrade);
       organizationFacade.edit(loadOrganization);
       ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Organization Grade Added.", ""));
       }
       catch(Exception ex){
           ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Add Org Grade failed.", ""));
       }
      
       newOrgGrade = new Orggrades();
       
   }
   public void deleteOrgGrade(){
        FacesContext ctx = FacesContext.getCurrentInstance();
        try{
             loadOrganization.getOrggradesList().remove(selectOrgGrade);
       orgFacade.remove(selectOrgGrade);
       organizationFacade.edit(loadOrganization);
        ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Organization grade deleted successfully.", ""));
        }
        catch(Exception ex){
             ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Delete Org Grade failed.", ""));
        }
      selectOrgGrade = new Orggrades();
   }
   public void selectOrgGrade(){
       selectOrgGrade = loadGradeDM.getRowData();
       editOrgGrade = loadGradeDM.getRowData();
   }
   public void addOrgParameter(){
        FacesContext ctx = FacesContext.getCurrentInstance();
        try{
       newOrgParam.setOrgid(loadOrganization);
       if(newOrgParam.getRequired().equalsIgnoreCase("True")){
           newOrgParam.setRequired("YES");
       }
       else{
            newOrgParam.setRequired("NO");
       }
       
       loadOrganization.getOrgparametersList().add(newOrgParam);
       organizationFacade.edit(loadOrganization);
       ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Organization Parameter Added.", ""));
        }
        catch(Exception ex){
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Add Parameter failed.", ""));
        }
       newOrgParam = new Orgparameters();
        
   }
   public void editOrgParameter(){
       FacesContext ctx = FacesContext.getCurrentInstance();
       try {
           loadOrganization.getOrgparametersList().remove(editOrgParam);
           if(editOrgParam.getRequired().equalsIgnoreCase("True")){
               editOrgParam.setRequired("YES");
           }
           else{
                editOrgParam.setRequired("NO");
           }
           orgParamFacade.edit(editOrgParam);
           loadOrganization.getOrgparametersList().add(editOrgParam);
           organizationFacade.edit(loadOrganization);
           
           ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, " Parameter Updated successfully.", ""));
       } catch (Exception ex) {
           ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Update Parameter failed.", ""));
       }
       editOrgParam = new Orgparameters();
   }
    public void deleteParameter(){
          FacesContext ctx = FacesContext.getCurrentInstance();
          try{
              loadOrganization.getOrgparametersList().remove(selectOrgParams);
          organizationFacade.edit(loadOrganization);
          orgParamFacade.remove(selectOrgParams);
          ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Organization Paremeter deleted successfully!.", ""));
          }
          catch(Exception ex){
               ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Delete Parameter failed.", ""));
          }
          selectOrgParams = new Orgparameters();
          
     }
    public void updateOrganization(){
       FacesContext ctx = FacesContext.getCurrentInstance();
       //loadOrganization.setCountryId(newCountry);
       Countries editOrgCountry = countriesFacade.find(subOrgCountryID);
       loadOrganization.setCountryId(editOrgCountry);
       organizationFacade.edit(loadOrganization);
       newCountry = new Countries();
        ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Organization Details Updated.", ""));
   }
    
    public void selectDeleteParam(){
        selectOrgParams = loadParametersDM.getRowData();
        editOrgParam = loadParametersDM.getRowData();
    }
   public void addOrganization(){
        FacesContext ctx = FacesContext.getCurrentInstance();
        try{
          Countries countryOrg = countriesFacade.find(countryId);
            
          
          
            newOrganization.setCountryId(countryOrg);
             organizationFacade.create(newOrganization);
           
             for(Organizations organ : childOrgs){
                organizationFacade.create(organ);
            }
             for(Orggrades grade : tempOrgGrades){
                orgFacade.create(grade);
            }
            for(Orgparameters param : tempOrgPars){
                orgParamFacade.create(param);
            }
            for(Orglevels level : tempOrgLevels){
                orgLevelFacade.create(level);
            }
            for(Orgunits unit : tempOrgUnits){
                orgUnitFacade.create(unit);
            }
           
            
            
            continueAdd = false;
            allGroupOrgs = new ArrayList<Organizations>();
            companyOrgs = new ArrayList<Organizations>();
            subsidiaryOrgs = new ArrayList<Organizations>();
            continueAdd = false;
            countryOrg = new Countries();
            newOrganization = new Organizations();
             countryId = null;
             subOrgCountryID = null;
            childOrgs = new ArrayList<Organizations>();
            tempOrgGrades = new ArrayList<Orggrades>();
            tempOrgUnits = new ArrayList<Orgunits>();
            tempOrgPars = new ArrayList<Orgparameters>();
            orgId = null;
            root = new DefaultTreeNode(new String(), null);
            addSucceed = true; //Render okay button
             ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Organization added successfully.", ""));
            
        } catch (Exception ex) {
             ex.printStackTrace();
            continueAdd = false;
             addSucceed = true; //Render okay button
           ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Could not add organization, an error occured", ""));
        }
       
       
   }
   public void editTree(){
       if(selectedNode.getType().equalsIgnoreCase("Default")){
           if(selectedNode.getData().toString().equalsIgnoreCase(parentNode.getData().toString())){ //If node is parent 
               //Set data
              
           }
           else{ //Node is child company
               
           }
       }
       else{
           
       }
   }
//   public void addToLoadedTree(){
//       FacesContext ctx = FacesContext.getCurrentInstance();
//      
//          System.out.println("Adding to tree");
////       System.out.println("Unit --> " + newOrgUnit.getOrgunitname());
////       System.out.println("Org --> " + subOrganization.getOrgname());
//       Orglevels hlevel = null;
//       int maxLevel = tempOrgLevels.size(); //Maximum Level
//       //FacesContext ctx = FacesContext.getCurrentInstance();
//       if (subOrganization.getOrgname() != null) { //detect if adding company
////           System.out.println("Adding company");
//           Countries subCountry = countriesFacade.find(subOrgCountryID);
//           if (!selectedLoadNode.getType().equalsIgnoreCase("orgs")) {
//               ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot create company here", ""));
//           }
//           else if(!selectedLoadNode.getData().toString().equalsIgnoreCase(loadOrganization.getOrgname())){
//                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot create company here", ""));
//           } 
//            else if(loadOrganization.getOrgname().equalsIgnoreCase(subOrganization.getOrgname())){
//               ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Company Name must not be same as group name", ""));
//           }
//           else {
////               for (Orglevels level : orgLevelFacade.findAll()) {
////                   if (level.getHlevel() == 2) {
////                       hlevel = level;
////                       break;
////                   }
////               }
//                 int intOrgId = 0 ;
//            if(organizationFacade.findAll().isEmpty() && childOrgs.isEmpty()){
//                
//                 intOrgId =(int) (loadOrganization.getOrgid()+1);
//            }
//            else if(organizationFacade.findAll().isEmpty() && !childOrgs.isEmpty()){
//                intOrgId = (int) (childOrgs.get(childOrgs.size()-1).getOrgid() +1);
//            }
//            else if(!organizationFacade.findAll().isEmpty() && childOrgs.isEmpty()){
//                 intOrgId = (int) (organizationFacade.findAll().get(organizationFacade.findAll().size()-1).getOrgid() + 1);
//            }
//            else if(!organizationFacade.findAll().isEmpty() && !childOrgs.isEmpty()){
//                intOrgId = (int) (childOrgs.get(childOrgs.size()-1).getOrgid() +1);
//            }
//            String stringOrgId = intOrgId + "";
//          //  System.out.println("Org ID-- >" + stringOrgId);
//            Long longOrgId = Long.parseLong(stringOrgId);
//               subOrganization.setOrgid(longOrgId);
//               subOrganization.setOrgtype("Company");
//               subOrganization.setParentOrgid(loadOrganization);
//               
//               subOrganization.setCountryId(subCountry);
////               System.out.println("Hlevel --> " + subOrganization.getHlevel().getHlevel());
//               childOrgs.add(subOrganization);
//               organizationFacade.create(subOrganization);
//               TreeNode childNode = new DefaultTreeNode("orgs",subOrganization.getOrgname(), selectedLoadNode);
//               selectedLoadNode.setExpanded(true);
//               ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Company added successfully.", ""));
//           }
//
//       } else if (newOrgUnit.getOrgunitname() != null) { //Detect if addding unit
//          System.out.println("Selected node --> " + selectedLoadNode);
//           boolean unitAdded = false;
//           
//                for (TreeNode node : selectedLoadNode.getChildren()) {
//               if (node.getData().toString().substring((node.getData().toString().indexOf(":"))).trim().equalsIgnoreCase(newOrgUnit.getOrgunitname())) {
//                   unitAdded = true;
//               }
//           }
//         
//          
//          
//           if (!selectedLoadNode.getType().equalsIgnoreCase("orgs") && newOrgUnit.getOrgunitname().equalsIgnoreCase(selectedLoadNode.getData().toString().substring((selectedLoadNode.getData().toString().indexOf(":"))).trim())) {
//               ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter different unit name !", ""));
//           }
//           else if(selectedLoadNode.getType().equalsIgnoreCase("orgs") && selectedLoadNode.getData().toString().equalsIgnoreCase(newOrgUnit.getOrgunitname()))
//           {
//               ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter different unit name !", ""));
//           }
//               else if (unitAdded) {
//               ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unit already added !", ""));
//           }
//            else if(orgLevelFacade.getLevelsByOrg(loadOrganization).isEmpty()){
//                            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Add level !", ""));
//                       }
//           else {
//               if (selectedLoadNode.getType().equalsIgnoreCase("orgs")) {
//                   //If node selected is root node 
//                   System.out.println("Selected node --> " +selectedLoadNode.getData().toString());
//                   if (selectedLoadNode.getData().toString().equalsIgnoreCase(parentLoadNode.getData().toString())) { //If node selected is parent
//                       newOrgUnit.setOrgid(loadOrganization);
//                       newOrgUnit.setOrgunitid(new Random().nextLong());
//                       if (newOrgUnit.getStatus().equalsIgnoreCase("true")) {
//                           newOrgUnit.setStatus("Active");
//                       } else {
//                           newOrgUnit.setStatus("InActive");
//                       }
//                       //Determine HLEVEL
//                       Orglevels unithlevel = null;
//                      for(Orglevels unitLevel : orgLevelFacade.getLevelsByOrg(loadOrganization)){
//                          if(Objects.equals(unitLevel.getOrgid().getOrgid(), loadOrganization.getOrgid())){
//                              unithlevel = unitLevel;
//                              break;
//                              
//                          }
//                      }
//                      newOrgUnit.setHlevel(unithlevel);
//                       int intUnitId = 0;
//                       
//                             
//                           intUnitId = orgUnitFacade.findAll().size() + 1;
//                      
//                       String unitStringId = intUnitId + "";
//                       Long longunitId = Long.parseLong(unitStringId);
//                       newOrgUnit.setOrgunitid(longunitId);
//                     unithlevel.getOrgid().getOrgunitsList().add(newOrgUnit);
//                     organizationFacade.edit( unithlevel.getOrgid());
//                      // System.out.println("Unit level -- > " + newOrgUnit.getHlevel().getHlevel());
//                       //tempOrgUnits.add(newOrgUnit);
//                       TreeNode newUnitNode = new DefaultTreeNode("units", newOrgUnit.getHlevel().getLevelname()+" : " +newOrgUnit.getOrgunitname(), selectedLoadNode);
//                         selectedLoadNode.setExpanded(true);
//                       newUnitNode.setExpanded(false);
//                   } else { //if node selected is company
//                       for (Organizations organ : organizationFacade.getSubOrganizations(loadOrganization)) {
//                           if (selectedLoadNode.getData().toString().equalsIgnoreCase(organ.getOrgname())) {
//                               newOrgUnit.setOrgid(organ);
//                               if (newOrgUnit.getStatus().equalsIgnoreCase("true")) {
//                                   newOrgUnit.setStatus("Active");
//                               } else {
//                                   newOrgUnit.setStatus("InActive");
//                               }
//                               int intUnitId = 0;
//                             
//                             
//                           intUnitId = tempOrgUnits.size() + 1;
//                      
//                               String unitStringId = intUnitId + "";
//                               Long longunitId = Long.parseLong(unitStringId);
//                               newOrgUnit.setOrgunitid(longunitId);
//                               Orglevels unithlevel = null;
//                               for (Orglevels unitLevel : orgLevelFacade.getLevelsByOrg(loadOrganization)) {
//                                   if (Objects.equals(unitLevel.getOrgid().getOrgid(), organ.getOrgid())) {
//                                       unithlevel = unitLevel;
//                                       break;
//                                       
//                                   }
//                               }
//                               newOrgUnit.setHlevel(unithlevel);
//                               System.out.println("HLEVEL --> " + newOrgUnit.getHlevel().getHlevel());
//                               unithlevel.getOrgid().getOrgunitsList().add(newOrgUnit);
//                               organizationFacade.edit( unithlevel.getOrgid());
//                             // orgUnitFacade.create(newOrgUnit);
//                              // tempOrgUnits.add(newOrgUnit);
//                               TreeNode newUnitNode = new DefaultTreeNode("units",newOrgUnit.getHlevel().getLevelname()+" : "+ newOrgUnit.getOrgunitname(), selectedLoadNode);
//                              newUnitNode.setExpanded(false);
//                              selectedLoadNode.setExpanded(true);
//                               break;
//                           }
//                       }
//
//                   }
//
//               } else {  //If adding subunit...
////                   System.out.println("Adding Sub unit"); //check parent org id bug here
//                 try{ //Bug here..
//                   for (Orgunits unit : orgUnitFacade.getUnitsByOrg(loadOrganization)) {
//                       if ((unit.getHlevel().getLevelname()+" : "+unit.getOrgunitname()).equalsIgnoreCase(selectedLoadNode.getData().toString())) {
//                           newOrgUnit.setParentOrgunitid(unit);
//                          // Orglevels lastLevel = tempOrgLevels.get(tempOrgLevels.size()-1);
//                           int index = orgLevelFacade.getLevelsByOrg(loadOrganization).indexOf(unit.getHlevel());
//                           
//                               
//                               newOrgUnit.setHlevel(orgLevelFacade.getLevelsByOrg(loadOrganization).get(index + 1));
//                               if (newOrgUnit.getStatus().equalsIgnoreCase("true")) {
//                                   newOrgUnit.setStatus("Active");
//                               } else {
//                                   newOrgUnit.setStatus("InActive");
//                                }
//                               System.out.println("HLEVEL --> " +newOrgUnit.getHlevel());
//                                int intUnitId = 0;
//                                
//                             
//                           intUnitId = orgUnitFacade.findAll().size() + 1;
//                      
//                               String unitStringId = intUnitId+"";
//                               Long longunitId = Long.parseLong(unitStringId);
//                              newOrgUnit.setOrgunitid(longunitId);
//                               newOrgUnit.setOrgid(unit.getHlevel().getOrgid());
//                               unit.getHlevel().getOrgid().getOrgunitsList().add(newOrgUnit);
//                               organizationFacade.edit(unit.getHlevel().getOrgid());
//                               //tempOrgUnits.add(newOrgUnit);
//                               TreeNode newUnitNode = new DefaultTreeNode("units", newOrgUnit.getHlevel().getLevelname()+" : "+newOrgUnit.getOrgunitname(), selectedLoadNode);
//                              //   orgUnitFacade.create(newOrgUnit);
//                              newUnitNode.setExpanded(false);
//                              selectedLoadNode.setExpanded(true);
//                               ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Unit added successfully", ""));
//                               break;
//                           
//                           
//                          
//                       }
//                   }
//
//                 }
//                 catch(IndexOutOfBoundsException ex){
//                     
//                      ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Max Level Reached", ""));
//                                
//                 }
//            
//                   
//               
//               }
//           }
//
//       }
//       newOrgUnit = new Orgunits();
//       subOrganization = new Organizations();
//       
//       
//   }
//   public void onchangeOrganizationAdd(){
//       allHlevels = new ArrayList<Orglevels>();
//        allTempUnits = new ArrayList<Orgunits>();
//       if(changeOrgID ==null){
//           allHlevels = new ArrayList<Orglevels>();
//       }
//       else{
//           for(Orglevels level : tempOrgLevels){
//               if(java.util.Objects.equals(level.getOrgid().getOrgid(), changeOrgID)){
//                   allHlevels.add(level);
//               }
//           }
//       
//       }
//       
//   }
   public void onChangeOrgLevelAdd(){
       allLoadUnits = new ArrayList<Orgunits>();
       if(changeLevelID==null){
           allLoadUnits = new ArrayList<Orgunits>();
       }
       else{
       for(Orgunits unit : orgUnitFacade.getUnitsByOrg(loadOrganization)){
           if(java.util.Objects.equals(changeLevelID, unit.getHlevel().getHlevel())){
            
                    allLoadUnits.add(unit);
               
              
           }
       }
       }
   }
   public void addToTree(){
//       System.out.println("Adding to tree");
//       System.out.println("Unit --> " + newOrgUnit.getOrgunitname());
//       System.out.println("Org --> " + subOrganization.getOrgname());
       Orglevels hlevel = null;
       int maxLevel = tempOrgLevels.size(); //Maximum Level
       FacesContext ctx = FacesContext.getCurrentInstance();
       if (subOrganization.getOrgname() != null) { //detect if adding company
//           System.out.println("Adding company");
           Countries subCountry = countriesFacade.find(subOrgCountryID);
           if (!selectedNode.getType().equalsIgnoreCase("Default")) {
               ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot create company here", ""));
           }
           else if(!selectedNode.getData().toString().equalsIgnoreCase(newOrganization.getOrgname())){
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot create company here", ""));
           } 
            else if(newOrganization.getOrgname().equalsIgnoreCase(subOrganization.getOrgname())){
               ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Company Name must not be same as group name", ""));
           }
           else {
//               for (Orglevels level : orgLevelFacade.findAll()) {
//                   if (level.getHlevel() == 2) {
//                       hlevel = level;
//                       break;
//                   }
//               }
                 int intOrgId = 0 ;
            if(organizationFacade.findAll().isEmpty() && childOrgs.isEmpty()){
                
                 intOrgId =(int) (newOrganization.getOrgid()+1);
            }
            else if(organizationFacade.findAll().isEmpty() && !childOrgs.isEmpty()){
                intOrgId = (int) (childOrgs.get(childOrgs.size()-1).getOrgid() +1);
            }
            else if(!organizationFacade.findAll().isEmpty() && childOrgs.isEmpty()){
                 intOrgId = (int) (organizationFacade.findAll().get(organizationFacade.findAll().size()-1).getOrgid() + 1);
            }
            else if(!organizationFacade.findAll().isEmpty() && !childOrgs.isEmpty()){
                intOrgId = (int) (childOrgs.get(childOrgs.size()-1).getOrgid() +1);
            }
            String stringOrgId = intOrgId + "";
            System.out.println("Org ID-- >" + stringOrgId);
            Long longOrgId = Long.parseLong(stringOrgId);
               subOrganization.setOrgid(longOrgId);
               subOrganization.setOrgtype("Company");
               subOrganization.setParentOrgid(newOrganization);
               
               subOrganization.setCountryId(subCountry);
//               System.out.println("Hlevel --> " + subOrganization.getHlevel().getHlevel());
               childOrgs.add(subOrganization);
               TreeNode childNode = new DefaultTreeNode(subOrganization.getOrgname(), parentNode);

               ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Company added successfully.", ""));
           }

       } else if (newOrgUnit.getOrgunitname() != null) { //Detect if addding unit
          System.out.println("Selected node --> " + selectedNode);
           boolean unitAdded = false;
           
                for (TreeNode node : selectedNode.getChildren()) {
               if (node.getData().toString().equalsIgnoreCase(newOrgUnit.getOrgunitname())) {
                   unitAdded = true;
               }
           }
         
          
          
           if (newOrgUnit.getOrgunitname().equalsIgnoreCase(selectedNode.getData().toString())) {
               ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter different unit name !", ""));
           } else if (unitAdded) {
               ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unit already added !", ""));
           }
            else if(tempOrgLevels.isEmpty()){
                            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Add level !", ""));
                       }
           else {
               if (selectedNode.getType().equalsIgnoreCase("default")) {
                   //If node selected is root node 
                   System.out.println("Selected node --> " +selectedNode.getData().toString());
                   if (selectedNode.getData().toString().equalsIgnoreCase(parentNode.getData().toString())) { //If node selected is parent
                       newOrgUnit.setOrgid(newOrganization);
                       newOrgUnit.setOrgunitid(new Random().nextLong());
                       if (newOrgUnit.getStatus().equalsIgnoreCase("true")) {
                           newOrgUnit.setStatus("Active");
                       } else {
                           newOrgUnit.setStatus("InActive");
                       }
                       //Determine HLEVEL
                       Orglevels unithlevel = null;
                      for(Orglevels unitLevel : tempOrgLevels){
//                          if(Objects.equals(unitLevel.getOrgid().getOrgid(), newOrganization.getOrgid())){
//                              unithlevel = unitLevel;
//                              break;
//                              
//                          }
                      }
                      newOrgUnit.setHlevel(unithlevel);
                       int intUnitId = 0;
                       if (orgUnitFacade.findAll().isEmpty() && tempOrgUnits.isEmpty()) {
                             
                           intUnitId = tempOrgUnits.size() + 1;
                       } 
                      
                       else if (orgUnitFacade.findAll().isEmpty() && !tempOrgUnits.isEmpty()) {
                           intUnitId = (int) (tempOrgUnits.get(tempOrgUnits.size()-1).getOrgunitid() +1);
                           
                       }
                       else if (!orgUnitFacade.findAll().isEmpty() && tempOrgUnits.isEmpty()) {
                           intUnitId = (int) (orgUnitFacade.findAll().get(orgUnitFacade.findAll().size()-1).getOrgunitid() +1);
                           
                       }
                       else if (!orgUnitFacade.findAll().isEmpty() && !tempOrgUnits.isEmpty()) {
                           intUnitId = (int) (tempOrgUnits.get(tempOrgUnits.size()-1).getOrgunitid() +1);
                       }
                       String unitStringId = intUnitId + "";
                       Long longunitId = Long.parseLong(unitStringId);
                       newOrgUnit.setOrgunitid(longunitId);
                      
                       System.out.println("Unit level -- > " + newOrgUnit.getHlevel().getHlevel());
                       tempOrgUnits.add(newOrgUnit);
                       TreeNode newUnitNode = new DefaultTreeNode("Units", newOrgUnit.getHlevel().getLevelname()+" : " +newOrgUnit.getOrgunitname(), selectedNode);
                       selectedNode.setExpanded(true);
                       newUnitNode.setExpanded(true);
                   } else { //if node selected is company
                       for (Organizations organ : childOrgs) {
                           if (selectedNode.getData().toString().equalsIgnoreCase(organ.getOrgname())) {
                               newOrgUnit.setOrgid(organ);
                               if (newOrgUnit.getStatus().equalsIgnoreCase("true")) {
                                   newOrgUnit.setStatus("Active");
                               } else {
                                   newOrgUnit.setStatus("InActive");
                               }
                               int intUnitId = 0;
                              if (orgUnitFacade.findAll().isEmpty() && tempOrgUnits.isEmpty()) {
                             
                           intUnitId = tempOrgUnits.size() + 1;
                       } else if (orgUnitFacade.findAll().isEmpty() && !tempOrgUnits.isEmpty()) {
                           intUnitId = (int) (tempOrgUnits.get(tempOrgUnits.size()-1).getOrgunitid() +1);
                           
                       }
                       else if (!orgUnitFacade.findAll().isEmpty() && tempOrgUnits.isEmpty()) {
                           intUnitId = (int) (orgUnitFacade.findAll().get(orgUnitFacade.findAll().size()-1).getOrgunitid() +1);
                           
                       }
                       else if (!orgUnitFacade.findAll().isEmpty() && !tempOrgUnits.isEmpty()) {
                           intUnitId = (int) (tempOrgUnits.get(tempOrgUnits.size()-1).getOrgunitid() +1);
                       }
                               String unitStringId = intUnitId + "";
                               Long longunitId = Long.parseLong(unitStringId);
                               newOrgUnit.setOrgunitid(longunitId);
                               Orglevels unithlevel = null;
//                               for (Orglevels unitLevel : tempOrgLevels) {
//                                   if (Objects.equals(unitLevel.getOrgid().getOrgid(), organ.getOrgid())) {
//                                       unithlevel = unitLevel;
//                                       break;
//                                       
//                                   }
//                               }
                               newOrgUnit.setHlevel(unithlevel);
                               System.out.println("HLEVEL --> " + newOrgUnit.getHlevel().getHlevel());
                              
                               tempOrgUnits.add(newOrgUnit);
                               TreeNode newUnitNode = new DefaultTreeNode("Units",newOrgUnit.getHlevel().getLevelname()+" : "+ newOrgUnit.getOrgunitname(), selectedNode);
                               newUnitNode.setExpanded(true);
                                selectedNode.setExpanded(true);
                               break;
                           }
                       }

                   }

               } else {  //If adding subunit...
//                   System.out.println("Adding Sub unit"); //check parent org id bug here
                 try{ //Bug here..
                   for (Orgunits unit : tempOrgUnits) {
                       if ((unit.getHlevel().getLevelname()+" : "+unit.getOrgunitname()).equalsIgnoreCase(selectedNode.getData().toString())) {
                           newOrgUnit.setParentOrgunitid(unit);
                          // Orglevels lastLevel = tempOrgLevels.get(tempOrgLevels.size()-1);
                          
                           int index = tempOrgLevels.indexOf(unit.getHlevel());
                               System.out.println("Level ID -->" + unit.getHlevel());
                                System.out.println(tempOrgLevels);
                               System.out.println("Top Level ->" +unit.getHlevel().getLevelname()+ "Index --> " + index );
                               newOrgUnit.setHlevel(tempOrgLevels.get(index + 1));
                               if (newOrgUnit.getStatus().equalsIgnoreCase("true")) {
                                   newOrgUnit.setStatus("Active");
                               } else {
                                   newOrgUnit.setStatus("InActive");
                               }
                               System.out.println("Bottom HLEVEL --> " +newOrgUnit.getHlevel().getLevelname());
                                int intUnitId = 0;
                                if (orgUnitFacade.findAll().isEmpty() && tempOrgUnits.isEmpty()) {
                             
                           intUnitId = tempOrgUnits.size() + 1;
                       } else if (orgUnitFacade.findAll().isEmpty() && !tempOrgUnits.isEmpty()) {
                           intUnitId = (int) (tempOrgUnits.get(tempOrgUnits.size()-1).getOrgunitid() +1);
                           
                       }
                       else if (!orgUnitFacade.findAll().isEmpty() && tempOrgUnits.isEmpty()) {
                           intUnitId = (int) (orgUnitFacade.findAll().get(orgUnitFacade.findAll().size()-1).getOrgunitid() +1);
                           
                       }
                       else if (!orgUnitFacade.findAll().isEmpty() && !tempOrgUnits.isEmpty()) {
                           intUnitId = (int) (tempOrgUnits.get(tempOrgUnits.size()-1).getOrgunitid() +1);
                       }
                               String unitStringId = intUnitId+"";
                               Long longunitId = Long.parseLong(unitStringId);
                              newOrgUnit.setOrgunitid(longunitId);
                          //     newOrgUnit.setOrgid(unit.getHlevel().getOrgid());
                               tempOrgUnits.add(newOrgUnit);
                               TreeNode newUnitNode = new DefaultTreeNode("Units", newOrgUnit.getHlevel().getLevelname()+" : "+newOrgUnit.getOrgunitname(), selectedNode);
                                 selectedNode.setExpanded(true);
                               newUnitNode.setExpanded(true);
                               ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Unit added successfully", ""));
                               break;
                           
                           
                          
                       }
                   }

                 }
                 catch(IndexOutOfBoundsException ex){
                     
                      ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Max Level Reached", ""));
                                
                 }
            
                   
               
               }
           }

       }
       newOrgUnit = new Orgunits();
       subOrganization = new Organizations();
   }
  
   public String flowListener(FlowEvent event){
       FacesContext ctx = FacesContext.getCurrentInstance();
       String step = "";
       if (event.getNewStep().equalsIgnoreCase("step2")) {
           RequestContext.getCurrentInstance().update("dlgLevel");
           Countries country = countriesFacade.find(countryId);
           newOrganization.setCountryId(country);
        if (orgId == null) {
             int intOrgId = 0 ;
            if(orgFacade.findAll().isEmpty() ){
                 intOrgId = 1;
            }
            else{
                 intOrgId = (int) (organizationFacade.findAll().get(organizationFacade.findAll().size()-1).getOrgid() + 1);
            }
           
            String orgIdString = intOrgId  + "";
            Long orgIdLong = Long.parseLong(orgIdString);
               
               newOrganization.setOrgid(orgIdLong);
           }
           if (tempOrgUnits.isEmpty() && childOrgs.isEmpty() ) {
              
                root = new DefaultTreeNode("Root", null);
               parentNode  = new DefaultTreeNode(newOrganization.getOrgname(), root);
               root.setExpanded(true);
               
              
//               else if(newOrganization.getOrgtype().equalsIgnoreCase("Company")){
//                   Organizations parentOrgan = organizationFacade.find(loadParentID);
//                   root = new DefaultTreeNode(parentOrgan.getOrgname(), null);
//                    TreeNode groupNode = new DefaultTreeNode(parentOrgan.getOrgname(), root);
//                   TreeNode childNode = new DefaultTreeNode(newOrganization.getOrgname(), groupNode);
//                   newOrganization.setParentOrgid(parentOrgan);
//                   root.setExpanded(true);
//                   childNode.setExpanded(true);
//               }
//               else if(newOrganization.getOrgtype().equalsIgnoreCase("Subsidiary")){
//                   Organizations companyOrgan = organizationFacade.find(loadParentID);
//                   Organizations groupOrgan = companyOrgan.getParentOrgid();
//                   
//                   root = new DefaultTreeNode(groupOrgan.getOrgname(), null);
//                    TreeNode groupNode = new DefaultTreeNode(groupOrgan.getOrgname(), root);
//                   TreeNode childNode = new DefaultTreeNode(companyOrgan.getOrgname(), groupNode);
//                   
//                    TreeNode subNode = new DefaultTreeNode(newOrganization.getOrgname(), childNode);
//                    newOrganization.setParentOrgid(groupOrgan);
//                   root.setExpanded(true);
//                   childNode.setExpanded(true);
//                    subNode.setExpanded(true);
//               }
         }
            step = event.getNewStep();
       }
      
//      if(event.getNewStep().equalsIgnoreCase("step4") && tempOrgLevels.isEmpty()){
//           ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please add at least one level!", step));
//         step = event.getOldStep();
//       }
      
     
       else{
      step = event.getNewStep();
       }
     
       
      
      return step;
  }
   
    //Manage countries
   public void addCountry(){
       FacesContext ctx = FacesContext.getCurrentInstance();
       if(countriesFacade.checkCountryExists(newCountry)){
           ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Country Exists", null));
       }
       else if(countriesFacade.checkDialCodeExists(newCountry)){
           ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Country with that dialing code Exists", null));
       }
       else{
           countriesFacade.create(newCountry);
           ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Country added successfully ", null));
       }
       
       newCountry = new Countries();
       
   }
   
   //ADD ORG PARAMETERS,Grades levels ON wizard
   
  public void loadOrgTypes(){
    childOrgs = new ArrayList<Organizations>();
    tempOrgUnits = new ArrayList<Orgunits>();
    lvlSupport = new ArrayList<LevelSupport>();
      if(newOrganization.getOrgtype().equalsIgnoreCase("Company")){
          
          System.out.println("Here..");
          allGroupOrgs = organizationFacade.getOrgTypes("Group");
//          System.out.println("Size--> " +allGroupOrgs.size());
          if(allGroupOrgs.isEmpty()){
              showGroupOrgs = false;
              
          }
          else{
              showGroupOrgs = true;
              showCompanyOrgs = false;
             
          }
      }
      
      else{
           showGroupOrgs = false;
           newOrganization.setParentOrgid(null);
           allGroupOrgs = new  ArrayList<Organizations>();
      }
      
   
     
//      else if(newOrganization.getOrgtype().equalsIgnoreCase("Subsidiary")){
//           System.out.println("Here 2..");
//          companyOrgs = organizationFacade.getOrgTypes("Company");
//           if(companyOrgs.isEmpty()){
//              showCompanyOrgs = false;
//          }
//          else{
//              showCompanyOrgs = true;
//              showGroupOrgs = false;
//          }
//      }
//      else{
//             showCompanyOrgs = false;
//            showGroupOrgs = false;
//      }
//      
      
  }
  
  
  public void loadSetParentOrg(){
      
         if(parentOrgID !=null){
          newOrganization.setParentOrgid(organizationFacade.find(parentOrgID));
           System.out.println("Parent org id --> " + newOrganization.getParentOrgid().getOrgid());
      }
      else{
          newOrganization.setParentOrgid(null);
      }
     
      
  }
  public void renderContextMenuItems(){
     
    
      
  }
  public void deleteTempLvl(){
      boolean hasUnits = false;
       FacesContext ctx = FacesContext.getCurrentInstance();
      deleteLVL = tempOrgLevelsDM.getRowData();
      for(Orgunits unit : tempOrgUnits){
          if(unit.getHlevel().getHlevel() == deleteLVL.getHlevel()){
              hasUnits = true;
              break;
          }
      }
      if(hasUnits){
          ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Remove units first !", ""));
      }
      else{
         tempOrgLevels.remove(deleteLVL);
         ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Level deleted successfully", ""));
      }
      deleteLVL = new Orglevels();
      
  }
  public void deleteUnit(){
      FacesContext ctx = FacesContext.getCurrentInstance();
      Orgunits loadUnit = null;
      boolean haschildren = false;
      int index = selectedNode.getData().toString().indexOf(":");
      String subStr = selectedNode.getData().toString().substring(index+1).trim();
     System.out.println("Unit Selected --> " +subStr) ;
      
      
      for(Orgunits unit :tempOrgUnits){
          if(unit.getOrgunitname().equalsIgnoreCase(subStr)){
              loadUnit = unit;
              break;
          }
      }
      
      if(!selectedNode.isLeaf()){
           ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please remove child units first!", null));
      }
      else{
//          System.out.println("Unit to delete --> " + loadUnit.getOrgunitname());
          tempOrgUnits.remove(loadUnit);
          
          removeElemetOfTreeNode(root, selectedNode);
         // temOrgUnitsDM = new ListDataModel<>(tempOrgUnits);
           ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Unit deleted ", null));
      }
     
      
      
  }
  
  public boolean removeElemetOfTreeNode(TreeNode rootNode,
        TreeNode nodeToDelete) {
    if (rootNode.getChildren().remove(nodeToDelete)) {
        return true;
    } else {
        for (TreeNode childNode : rootNode.getChildren()) {
            if (childNode.getChildCount() > 0) {
                return removeElemetOfTreeNode(childNode, nodeToDelete);
            }

        }
        return false;
    }
}

 public void selectDeleteUnit(){
     System.out.println("Selected Node --> " + selectedNode.getData().toString());
     
     
 }
  public void addTempOrUnits(){
      FacesContext ctx = FacesContext.getCurrentInstance();
      System.out.println("Adding tree node...");
       boolean unitAdded = false;
      
            for(Orgunits unit : tempOrgUnits){
                if(unit.getOrgunitname().equalsIgnoreCase(newOrgUnit.getOrgunitname())){
                    for(TreeNode node : selectedNode.getChildren()){
                        if(node.getData().toString().equalsIgnoreCase(newOrgUnit.getOrgunitname())){
                            unitAdded = true;
                            break;
                        }
                    }
                     
                    break;
                }
            } 
          
            
            if(unitAdded){
                 ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unit already added", null));
            }
           
//            else if(!selectedNode.getData().toString().equalsIgnoreCase(newOrganization.getOrgname())){
//                 ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot create unit here", null));
//            }
            else if(selectedNode.getData().toString().equalsIgnoreCase(newOrgUnit.getOrgunitname())){
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please Enter a different unit name", null));
            }
            else{
              
                   if(tempOrgUnits.isEmpty()){
                        newOrgUnit.setOrgid(newOrganization);
                         newOrgUnit.setOrgunitid(new Random().nextLong());
                         if(newOrgUnit.getStatus().equalsIgnoreCase("true")){
                             newOrgUnit.setStatus("Active");
                         }
                         else{
                             newOrgUnit.setStatus("InActive");
                         }
                         tempOrgUnits.add(newOrgUnit);
                         TreeNode newNode = new DefaultTreeNode(newOrgUnit.getOrgunitname(), selectedNode);
                         
                         newNode.setExpanded(true);
                   }
                   else{
                      
                         for(Orgunits unit : tempOrgUnits){
                             if(unit.getOrgunitname().equalsIgnoreCase(selectedNode.getData().toString())){
                                 newOrgUnit.setParentOrgunitid(unit);
                                 break;
                             }
                         }
                         
                         newOrgUnit.setOrgid(newOrganization);
                         newOrgUnit.setOrgunitid(new Random().nextLong());
                         if(newOrgUnit.getStatus().equalsIgnoreCase("true")){
                             newOrgUnit.setStatus("Active");
                         }
                         else{
                             newOrgUnit.setStatus("InActive");
                         }
                         tempOrgUnits.add(newOrgUnit);
                         TreeNode newNode = new DefaultTreeNode(newOrgUnit.getOrgunitname(), selectedNode);
                          
                         newNode.setExpanded(true);
                         
                    
                   }
                   
               ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Unit added  successfully", null));
            }
            newOrgUnit = new  Orgunits();
  }
  public void addTempOrgLevel(){
       FacesContext ctx = FacesContext.getCurrentInstance();
//        if(java.util.Objects.equals(newOrganization.getOrgid(), tempOrgID)){
//                   newOrgLevel.setOrgid(newOrganization);
//               }
//               else{
//                   for(Organizations organ : childOrgs){
//                       if(java.util.Objects.equals(organ.getOrgid(), tempOrgID)){
//                            newOrgLevel.setOrgid(organ);
//                            break;
//                       }
//                   }
//               }
       boolean levelAdded = false;
//       for(Orglevels level : tempOrgLevels){
//           if(level.getLevelname().equalsIgnoreCase(newOrgLevel.getLevelname()) && newOrgLevel.getOrgid().getOrgid() == level.getOrgid().getOrgid()){
//               levelAdded = true;
//               break;
//           }
//       }
          if(levelAdded){
               ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Level already added .", null));
          }  
          else if(newOrgLevel.getLevelname().equalsIgnoreCase(newOrganization.getOrgname())){
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Level must not be same as parent organization", null));
            }
            else{
                int levelIntID = 0;
              if(tempOrgLevels.isEmpty()){
                  levelIntID =  1;
              }
              
              else if(!tempOrgLevels.isEmpty()){
                  levelIntID = (int) (tempOrgLevels.get(tempOrgLevels.size()-1).getHlevel()+1);
              }
//               else if(!orgLevelFacade.findAll().isEmpty() && tempOrgLevels.isEmpty()){
//                  levelIntID =  (int) (orgLevelFacade.findAll().get(orgLevelFacade.findAll().size() -1).getHlevel()+1);
//              }
//              else if(!orgLevelFacade.findAll().isEmpty() && !tempOrgLevels.isEmpty()){
//                  levelIntID = (int) (tempOrgLevels.get(tempOrgLevels.size()-1).getHlevel()+1);
//              }
              int levelId = 0;
            if(tempOrgLevels.isEmpty()){
                levelId = orgLevelFacade.findAll().size() + 1;
            }
             
            else if(!orgLevelFacade.findAll().isEmpty() && tempOrgLevels.isEmpty()){
                levelId = (int) (orgLevelFacade.findAll().get(orgLevelFacade.findAll().size() -1).getLevelid()+1);
            }
            else if(!orgLevelFacade.findAll().isEmpty() && !tempOrgLevels.isEmpty()){
                levelId = (int) (tempOrgLevels.get(tempOrgLevels.size()-1).getLevelid()+1);
            }
            else if(orgLevelFacade.findAll().isEmpty() && !tempOrgLevels.isEmpty()){
                 levelId = (int) (tempOrgLevels.get(tempOrgLevels.size()-1).getLevelid()+1);
            } 
            
                String levelIdString = levelId + "";
                Long longLvvId = Long.parseLong(levelIdString);
                String levelIntString = levelIntID +"";
                Long longLevelId = Long.parseLong(levelIntString);
                newOrgLevel.setLevelid(longLvvId);
                newOrgLevel.setHlevel(longLevelId);
                System.out.println("Temp org id -->" + tempOrgID);
                
              
               
           
               
                //   System.out.println("Org Level --> "+ newOrgLevel.getLevelName()+" Org levels -->" + newOrganization.getOrglevelsList());
                tempOrgLevels.add(newOrgLevel);
                
                 
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Level added successfully ", null));
                
            }
            newOrgLevel = new Orglevels();
            tempOrgID= null;
             RequestContext.getCurrentInstance().update("treeContext");
               RequestContext.getCurrentInstance().update("treeContext2");
  }
   public void addTempOrgGrades(){
        FacesContext ctx = FacesContext.getCurrentInstance();
        try{
            boolean gradeAdded = false;
            for(Orggrades grade : tempOrgGrades){
                if(grade.getGradename().equalsIgnoreCase(newOrgGrade.getGradename())){
                    gradeAdded = true;
                    break;
                }
            }
            if(gradeAdded){
                 ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Grade already added", null));
            }
            else{
                int intOrgIng = 0;
                if(orgLevelFacade.findAll().isEmpty() && tempOrgGrades.isEmpty()){
                  intOrgIng =  1;
              }
              
              else if(orgFacade.findAll().isEmpty() && !tempOrgGrades.isEmpty()){
                  intOrgIng = (int)(tempOrgGrades.get(tempOrgGrades.size()-1).getGradeid()+1);
              }
               else if(!orgFacade.findAll().isEmpty() && tempOrgGrades.isEmpty()){
                  intOrgIng =  (int) (orgFacade.findAll().get(orgFacade.findAll().size() -1).getGradeid()+1);
              }
              else if(!orgFacade.findAll().isEmpty() && !tempOrgGrades.isEmpty()){
                  intOrgIng = (int) (tempOrgGrades.get(tempOrgGrades.size()-1).getGradeid()+1);
              }
                String stringGradeid  = intOrgIng + "";
                Long longGradeId = Long.parseLong(stringGradeid);
                newOrgGrade.setGradeid(longGradeId);
                newOrgGrade.setOrgid(newOrganization);
                tempOrgGrades.add(newOrgGrade);
                
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Grade added successfully ", null));
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        newOrgGrade = new Orggrades();
   }
   public void removeTempGrade(){
       FacesContext ctx = FacesContext.getCurrentInstance();
       Orggrades remGrade = tempOrgGradeDM.getRowData();
       tempOrgGrades.remove(remGrade);
       ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Grade removed successfully ", null));
   }
   public void addTempOrgParameter(){
        FacesContext ctx = FacesContext.getCurrentInstance();
        try{
            boolean parAdded = false;
            for(Orgparameters param : tempOrgPars){
                if(param.getParametername().equalsIgnoreCase(newOrgParam.getParametername())){
                    parAdded = true;
                    break;
                }
            }
            if(parAdded){
                 ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Parameter already added", null));
            }
            else{
                int intOrgParam = 0;
              if(tempOrgPars.isEmpty() && orgParamFacade.findAll().isEmpty()){
                   intOrgParam = 1;
              }  
              else if(!tempOrgPars.isEmpty() && orgParamFacade.findAll().isEmpty()){
                 intOrgParam = (int) (tempOrgPars.get(tempOrgPars.size()-1).getParameterid() +1);
              }
              else if(tempOrgPars.isEmpty() && !orgParamFacade.findAll().isEmpty()){
                  intOrgParam = (int) (orgParamFacade.findAll().get(orgParamFacade.findAll().size()-1).getParameterid() + 1);
              }
              else if(!tempOrgPars.isEmpty() && !orgParamFacade.findAll().isEmpty()){
                  intOrgParam = (int) (tempOrgPars.get(tempOrgPars.size()-1).getParameterid() +1);
              }
              String stringParId = intOrgParam +"";
              Long longParId = Long.parseLong(stringParId);
              newOrgParam.setParameterid(longParId);
              newOrgParam.setOrgid(newOrganization);
              if(newOrgParam.getRequired().equalsIgnoreCase("true")){
                  newOrgParam.setRequired("YES");
              }
              else{
                   newOrgParam.setRequired("NO");
              }
              tempOrgPars.add(newOrgParam);
              ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Parameter added successfully ", null));
            }
     
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        newOrgParam = new Orgparameters();
   }
   
   public void removeTempPar(){
       FacesContext ctx = FacesContext.getCurrentInstance();
       try{
       Orgparameters remPars = tempOrgParamDM.getRowData();
       tempOrgPars.remove(remPars);
        ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Parameter removed successfully ", null));
       }
       catch(Exception ex){
           
       }
       }
       
  //DM SETTERS
    public DataModel<Countries> getCountriesDM() {
        countriesDM = new ListDataModel<Countries>(countriesFacade.findAll());
        return countriesDM;
    }

    public void setCountriesDM(DataModel<Countries> countriesDM) {
        this.countriesDM = countriesDM;
    }

    public DataModel<Orgparameters> getTempOrgParamDM() {
        tempOrgParamDM = new ListDataModel<Orgparameters>(tempOrgPars);
        return tempOrgParamDM;
    }

    public void setTempOrgParamDM(DataModel<Orgparameters> tempOrgParamDM) {
        this.tempOrgParamDM = tempOrgParamDM;
    }

    public DataModel<Orggrades> getLoadGradeDM() {
        loadGradeDM = new ListDataModel<Orggrades>(orgFacade.getGradesByOrg(loadOrganization));
        return loadGradeDM;
    }

    public void setLoadGradeDM(DataModel<Orggrades> loadGradeDM) {
        this.loadGradeDM = loadGradeDM;
    }

    
    
    
    //Entity Setters
    
    
    public Orglevels getSelectOrglevels() {
        return selectOrglevels;
    }

    public void setSelectOrglevels(Orglevels selectOrglevels) {
        this.selectOrglevels = selectOrglevels;
    }

    public Orggrades getSelectOrgGrade() {
        return selectOrgGrade;
    }

    public void setSelectOrgGrade(Orggrades selectOrgGrade) {
        this.selectOrgGrade = selectOrgGrade;
    }

    public Orggrades getEditOrgGrade() {
        return editOrgGrade;
    }

    public void setEditOrgGrade(Orggrades editOrgGrade) {
        this.editOrgGrade = editOrgGrade;
    }

    public Orggrades getNewOrgGrade() {
        return newOrgGrade;
    }

    public void setNewOrgGrade(Orggrades newOrgGrade) {
        this.newOrgGrade = newOrgGrade;
    }

    public Organizations getNewOrganization() {
        return newOrganization;
    }

    public void setNewOrganization(Organizations newOrganization) {
        this.newOrganization = newOrganization;
    }
    
    
    public Countries getNewCountry() {
        return newCountry;
    }

    public void setNewCountry(Countries newCountry) {
        this.newCountry = newCountry;
    }

    public Countries getEditCountry() {
        return editCountry;
    }

    public void setEditCountry(Countries editCountry) {
        this.editCountry = editCountry;
    }

    public Countries getSelectCountry() {
        return selectCountry;
    }

    public void setSelectCountry(Countries selectCountry) {
        this.selectCountry = selectCountry;
    }

    public Orgparameters getNewOrgParam() {
        return newOrgParam;
    }

    public void setNewOrgParam(Orgparameters newOrgParam) {
        this.newOrgParam = newOrgParam;
    }

    public Orglevels getNewOrgLevel() {
        return newOrgLevel;
    }

    public void setNewOrgLevel(Orglevels newOrgLevel) {
        this.newOrgLevel = newOrgLevel;
    }

    public Orgunits getNewOrgUnit() {
        return newOrgUnit;
    }

    public void setNewOrgUnit(Orgunits newOrgUnit) {
        this.newOrgUnit = newOrgUnit;
    }

    public Organizations getLoadOrganization() {
        return loadOrganization;
    }

    public void setLoadOrganization(Organizations loadOrganization) {
        this.loadOrganization = loadOrganization;
    }

    public Orgparameters getEditOrgParam() {
        return editOrgParam;
    }

    public void setEditOrgParam(Orgparameters editOrgParam) {
        this.editOrgParam = editOrgParam;
    }

    public Orgparameters getSelectOrgParams() {
        return selectOrgParams;
    }

    public void setSelectOrgParams(Orgparameters selectOrgParams) {
        this.selectOrgParams = selectOrgParams;
    }

    public Orglevels getDeleteLVL() {
        return deleteLVL;
    }

    public void setDeleteLVL(Orglevels deleteLVL) {
        this.deleteLVL = deleteLVL;
    }

    public Orgunits getDeleteUnit() {
        return deleteUnit;
    }

    public void setDeleteUnit(Orgunits deleteUnit) {
        this.deleteUnit = deleteUnit;
    }

    public Organizations getSubOrganization() {
        return subOrganization;
    }

    public void setSubOrganization(Organizations subOrganization) {
        this.subOrganization = subOrganization;
    }

    public Orgunits getSelectUnit() {
        return selectUnit;
    }

    public void setSelectUnit(Orgunits selectUnit) {
        this.selectUnit = selectUnit;
    }

   
    
    
    
    //LIST GETTERS
    
    
 
    public List<Orgunits> getTempOrgUnits() {
        return tempOrgUnits;
    }

    public void setTempOrgUnits(List<Orgunits> tempOrgUnits) {
        this.tempOrgUnits = tempOrgUnits;
    }

    public List<Orglevels> getTempOrgLevels() {
        
        return tempOrgLevels;
    }

    public void setTempOrgLevels(List<Orglevels> tempOrgLevels) {
        this.tempOrgLevels = tempOrgLevels;
    }

    public List<Orglevels> getChildLevels() {
        return childLevels;
    }

    public void setChildLevels(List<Orglevels> childLevels) {
        this.childLevels = childLevels;
    }

    public List<Organizations> getCompanyOrgs() {
        return companyOrgs;
    }

    public void setCompanyOrgs(List<Organizations> companyOrgs) {
        this.companyOrgs = companyOrgs;
    }

    public List<Organizations> getSubsidiaryOrgs() {
        return subsidiaryOrgs;
    }

    public void setSubsidiaryOrgs(List<Organizations> subsidiaryOrgs) {
        this.subsidiaryOrgs = subsidiaryOrgs;
    }

  

    public List<Organizations> getAllGroupOrgs() {
       
        return allGroupOrgs;
    }

    public void setAllGroupOrgs(List<Organizations> allGroupOrgs) {
        this.allGroupOrgs = allGroupOrgs;
    }

    public DataModel<Organizations> getDbOrgsDM() {
        dbOrgsDM = new ListDataModel<Organizations>(organizationFacade.findAll());
        return dbOrgsDM;
    }

    public void setDbOrgsDM(DataModel<Organizations> dbOrgsDM) {
        this.dbOrgsDM = dbOrgsDM;
    }

    public DataModel<Orgparameters> getLoadParametersDM() {
        loadParametersDM = new ListDataModel<Orgparameters>(orgParamFacade.getParByOrg(loadOrganization));
        return loadParametersDM;
    }

    public void setLoadParametersDM(DataModel<Orgparameters> loadParametersDM) {
        this.loadParametersDM = loadParametersDM;
    }

    public DataModel<Establishments> getLoadEstablishmentsDM() {
        loadEstablishmentsDM = new ListDataModel<Establishments>(loadOrganization.getEstablishmentsList());
        return loadEstablishmentsDM;
    }

    public void setLoadEstablishmentsDM(DataModel<Establishments> loadEstablishmentsDM) {
        this.loadEstablishmentsDM = loadEstablishmentsDM;
    }

    public DataModel<Orglevels> getOrgLevelDM() {
        
        orgLevelDM = new ListDataModel<Orglevels>(orgLevelFacade.findAll());
        return orgLevelDM;
    }

    public void setOrgLevelDM(DataModel<Orglevels> orgLevelDM) {
        this.orgLevelDM = orgLevelDM;
    }

    public DataModel<Orgunits> getOrgUnitsDM() {
       
        orgUnitsDM = new ListDataModel<Orgunits>(orgUnitFacade.getUnitsByOrg(loadOrganization));
        return orgUnitsDM;
    }

    public void setOrgUnitsDM(DataModel<Orgunits> orgUnitsDM) {
        this.orgUnitsDM = orgUnitsDM;
    }

    public DataModel<Orglevels> getTempOrgLevelsDM() {
        tempOrgLevelsDM = new ListDataModel<Orglevels>(tempOrgLevels);
        return tempOrgLevelsDM;
    }

    public void setTempOrgLevelsDM(DataModel<Orglevels> tempOrgLevelsDM) {
        this.tempOrgLevelsDM = tempOrgLevelsDM;
    }

    public DataModel<Orgunits> getTemOrgUnitsDM() {
        temOrgUnitsDM = new ListDataModel<Orgunits>(tempOrgUnits);
        return temOrgUnitsDM;
    }

    public void setTemOrgUnitsDM(DataModel<Orgunits> temOrgUnitsDM) {
        this.temOrgUnitsDM = temOrgUnitsDM;
    }

    public DataModel<Organizations> getSubOrganizationDM() {
        subOrganizationDM = new ListDataModel<Organizations>(organizationFacade.getSubOrganizations(loadOrganization));
        return subOrganizationDM;
    }

    public void setSubOrganizationDM(DataModel<Organizations> subOrganizationDM) {
        this.subOrganizationDM = subOrganizationDM;
    }

   
     
    
    
    public DataModel<Orggrades> getTempOrgGradeDM() {
        tempOrgGradeDM = new ListDataModel<Orggrades>(tempOrgGrades);
        return tempOrgGradeDM;
    }

    public void setTempOrgGradeDM(DataModel<Orggrades> tempOrgGradeDM) {
        this.tempOrgGradeDM = tempOrgGradeDM;
    }

    public List<Orggrades> getTempOrgGrades() {
        return tempOrgGrades;
    }

    public void setTempOrgGrades(List<Orggrades> tempOrgGrades) {
        this.tempOrgGrades = tempOrgGrades;
    }

    public List<Organizations> getChildOrgs() {
        return childOrgs;
    }

    public void setChildOrgs(List<Organizations> childOrgs) {
        this.childOrgs = childOrgs;
    }

    
    public List<Orglevels> getAllHlevels() {
        allHlevels = orgLevelFacade.getLevelsByOrg(loadOrganization);
        
        return allHlevels;
    }

    public void setAllHlevels(List<Orglevels> allHlevels) {
        this.allHlevels = allHlevels;
    }

    
    public List<Countries> getAllCountries() {
        allCountries = countriesFacade.findAll();
        return allCountries;
    }

    public void setAllCountries(List<Countries> allCountries) {
        this.allCountries = allCountries;
    }

    public List<Orgparameters> getTempOrgPars() {
        return tempOrgPars;
    }

    public void setTempOrgPars(List<Orgparameters> tempOrgPars) {
        this.tempOrgPars = tempOrgPars;
    } 

    public List<LevelSupport> getLvlSupport() {
        return lvlSupport;
    }

    public void setLvlSupport(List<LevelSupport> lvlSupport) {
        this.lvlSupport = lvlSupport;
    }

    public List<Orgunits> getAllLoadUnits() {
        allLoadUnits = orgUnitFacade.getUnitsByOrg(loadOrganization);
        return allLoadUnits;
    }

    public void setAllLoadUnits(List<Orgunits> allLoadUnits) {
        this.allLoadUnits = allLoadUnits;
    }

    public List<Orgunits> getAllTempUnits() {
        return allTempUnits;
    }

    public void setAllTempUnits(List<Orgunits> allTempUnits) {
        this.allTempUnits = allTempUnits;
    }
    
    
    
    //VARIABLE SETTERS
    public boolean isAddSucceed() {
        return addSucceed;
    }

    public void setAddSucceed(boolean addSucceed) {
        this.addSucceed = addSucceed;
    }
    
    
    public boolean isContinueAdd() {
        return continueAdd;
    }

    public void setContinueAdd(boolean continueAdd) {
        this.continueAdd = continueAdd;
    }

    public Long getSubOrgCountryID() {
        return subOrgCountryID;
    }

    public void setSubOrgCountryID(Long subOrgCountryID) {
        this.subOrgCountryID = subOrgCountryID;
    }
    
    
    public boolean isAgreeTerms() {
        return agreeTerms;
    }

    public void setAgreeTerms(boolean agreeTerms) {
        this.agreeTerms = agreeTerms;
    }
    
    
    public boolean isShowGroupOrgs() {
        return showGroupOrgs;
    }

    public void setShowGroupOrgs(boolean showGroupOrgs) {
        this.showGroupOrgs = showGroupOrgs;
    }

    public boolean isShowCompanyOrgs() {
        return showCompanyOrgs;
    }

    public void setShowCompanyOrgs(boolean showCompanyOrgs) {
        this.showCompanyOrgs = showCompanyOrgs;
    }

    public boolean isLevelAdded() {
        return levelAdded;
    }

    public void setLevelAdded(boolean levelAdded) {
        this.levelAdded = levelAdded;
    }

    
    public boolean isShowSubOrgs() {
        return showSubOrgs;
    }

    public void setShowSubOrgs(boolean showSubOrgs) {
        this.showSubOrgs = showSubOrgs;
    }

    public TreeNode getChildOrgNode() {
        return childOrgNode;
    }

    public void setChildOrgNode(TreeNode childOrgNode) {
        this.childOrgNode = childOrgNode;
    }

    public TreeNode getcUnitNode() {
        return cUnitNode;
    }

    public void setcUnitNode(TreeNode cUnitNode) {
        this.cUnitNode = cUnitNode;
    }

    public TreeNode getChildHeadNode() {
        return childHeadNode;
    }

    public void setChildHeadNode(TreeNode childHeadNode) {
        this.childHeadNode = childHeadNode;
    }

    
    public TreeNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(TreeNode parentNode) {
        this.parentNode = parentNode;
    }

    public TreeNode getParentLoadNode() {
        return parentLoadNode;
    }

    public void setParentLoadNode(TreeNode parentLoadNode) {
        this.parentLoadNode = parentLoadNode;
    }
   
    
    
    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public TreeNode getSelectedLoadNode() {
        return selectedLoadNode;
    }

    public void setSelectedLoadNode(TreeNode selectedLoadNode) {
        this.selectedLoadNode = selectedLoadNode;
    }

    public TreeNode getRootLoad() {
        
       
       
       
        return rootLoad;
    }
    
    
    
    
    public void setRootLoad(TreeNode rootLoad) {
        this.rootLoad = rootLoad;
    }
    
    

   

    public String getTempPath() {
        return tempPath;
    }

    public void setTempPath(String tempPath) {
        this.tempPath = tempPath;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
    
  
    
    
    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public Long getParentOrgID() {
        return parentOrgID;
    }

    public void setParentOrgID(Long parentOrgID) {
        this.parentOrgID = parentOrgID;
    }
    
    
    
    public Long getLoadChildOrgID() {
        return loadChildOrgID;
    }

    public void setLoadChildOrgID(Long loadChildOrgID) {
        this.loadChildOrgID = loadChildOrgID;
    }

    public Long getLoadParentID() {
        return loadParentID;
    }

    public void setLoadParentID(Long loadParentID) {
        this.loadParentID = loadParentID;
    }
   
    
    
    
    public Long getLoadOrgID() {
        return loadOrgID;
    }

    public void setLoadOrgID(Long loadOrgID) {
        this.loadOrgID = loadOrgID;
    }

    public Long getLoadLevelID() {
        return loadLevelID;
    }

    public void setLoadLevelID(Long loadLevelID) {
        this.loadLevelID = loadLevelID;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public Long getTempOrgID() {
        return tempOrgID;
    }

    public void setTempOrgID(Long tempOrgID) {
        this.tempOrgID = tempOrgID;
    }

    public Long getChangeOrgID() {
        return changeOrgID;
    }

    public void setChangeOrgID(Long changeOrgID) {
        this.changeOrgID = changeOrgID;
    }

    public Long getChangeLevelID() {
        return changeLevelID;
    }

    public void setChangeLevelID(Long changeLevelID) {
        this.changeLevelID = changeLevelID;
    }

    public Long getChangeUnitID() {
        return changeUnitID;
    }

    public void setChangeUnitID(Long changeUnitID) {
        this.changeUnitID = changeUnitID;
    }

    public String getOldLevelName() {
        return oldLevelName;
    }

    public void setOldLevelName(String oldLevelName) {
        this.oldLevelName = oldLevelName;
    }

    public String getOldUnitName() {
        return oldUnitName;
    }

    public boolean isvUnitAdd() {
        return vUnitAdd;
    }

    public void setvUnitAdd(boolean vUnitAdd) {
        this.vUnitAdd = vUnitAdd;
    }

    
    public void setOldUnitName(String oldUnitName) {
        this.oldUnitName = oldUnitName;
    }

    public StreamedContent getImage() {
        image = new DefaultStreamedContent();
        FacesContext ctx = FacesContext.getCurrentInstance();
        if(ctx.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE){
            return new DefaultStreamedContent();
        }
        else{
          
            return new DefaultStreamedContent(new ByteInputStream(loadOrganization.getLogo(), loadOrganization.getLogo().length));
        }
        
    }

    public void setImage(StreamedContent image) {
        this.image = image;
    }

    
    
    
    
    
   
}
